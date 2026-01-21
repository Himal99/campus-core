package com.sb.file.compressor.compress.service.implementation;

import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.PdfImageObject;
import com.sb.file.compressor.compress.dtos.CompressedVideoResponseDto;
import com.sb.file.compressor.compress.dtos.ResponseDto;
import com.sb.file.compressor.compress.dtos.SavedFileResponseDto;
import com.sb.file.compressor.compress.service.contract.CompressApiService;
import com.sb.file.compressor.compress.utils.ApiEndpoints;
import com.sb.file.compressor.compress.utils.Base64Decoder;
import com.sb.file.compressor.compress.utils.FileSaver;
import com.sb.file.compressor.core.exception.CustomException;
import com.sb.file.compressor.core.utils.SystemUtils;
import com.sb.file.compressor.entity.UserConfiguration;
import com.sb.file.compressor.model.compressionHistory.entity.CompressionHistory;
import com.sb.file.compressor.model.compressionHistory.enums.HistoryStatus;
import com.sb.file.compressor.model.compressionHistory.service.contract.CompressionHistoryService;
import com.sb.file.compressor.model.quickCompressReports.entity.QuickCompressionReports;
import com.sb.file.compressor.model.quickCompressReports.service.contract.QuickCompressionReportsService;
import com.sb.file.compressor.model.systemConfig.entity.SystemConfiguration;
import com.sb.file.compressor.model.systemConfig.service.SystemConfigurationService;
import com.sb.file.compressor.service.contract.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.*;

/**
 * @author Himal Rai on 1/24/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */
@Service
@Slf4j
public class CompressApiServiceImpl implements CompressApiService {
    public static final float FACTOR = 0.5f;
    private static final String SAVED_FOLDER = "compressed";

    private final SystemConfigurationService systemConfigurationService;
    private final CompressionHistoryService compressionHistoryService;

    @Value("${compress.api}")
    private String baseUri;

    @Value("${compression.server.excel.path}")
    private String serverExcelPath;



    private WebClient webClient;
    private final QuickCompressionReportsService quickCompressionReportsService;
    private final UserService userService;

    CompressApiServiceImpl(SystemConfigurationService systemConfigurationService, CompressionHistoryService compressionHistoryService, QuickCompressionReportsService quickCompressionReportsService, UserService userService) {
        this.systemConfigurationService = systemConfigurationService;
        this.compressionHistoryService = compressionHistoryService;
        this.quickCompressionReportsService = quickCompressionReportsService;
        this.userService = userService;
        this.webClient = WebClient.builder().codecs(codecs -> codecs
                .defaultCodecs()
                .maxInMemorySize(50000 * 1024)).build();

    }


    @Override
    public List<SavedFileResponseDto> compressImages(MultiValueMap<String, Object> body, String sessionId) {
        try {

            URI uri = new URI(baseUri + ApiEndpoints.COMPRESS_IMAGE);
            ResponseDto response = webClient.post().uri(uri).contentType(MediaType.MULTIPART_FORM_DATA).body(BodyInserters.fromMultipartData(body))
                    .retrieve().bodyToMono(ResponseDto.class).block();

            response.setImage(true);
            List<SavedFileResponseDto> savedFileResponse = new ArrayList<>();

            if (!ObjectUtils.isEmpty(response.getCompressed_files())) {
                List<ResponseDto.compressed_files> compressed_files = response.getCompressed_files();
                compressed_files.forEach(compressedFile -> {
                    byte[] decodedData = Base64Decoder.decodeBase64(compressedFile.getData());

                    try {
//                        double compressedSize = SystemUtils.getSizeInMbFromBytes(decodedData);
                        double compressedSize = decodedData.length;
                        String path = FileSaver.uploadFile(decodedData, SAVED_FOLDER + "/" + sessionId, compressedFile.getFile_name());
                        SavedFileResponseDto savedFileResponseDto = SavedFileResponseDto
                                .builder()
                                .compressedSize(compressedSize)
                                .name(compressedFile.getFile_name())
                                .filePath(path)
                                .sessionId(sessionId)
                                .size(0.0)
                                .build();

                        savedFileResponse.add(savedFileResponseDto);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

            }

            return savedFileResponse;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error::{}", e.getMessage());

            throw new CustomException("can't compress files");
        }

    }


    @Override
    public Object compressServerFile(String fileDirectory, String backUpDirectory, String endpointUrl, Long systemServerId, String currentUsername, String sessionId) {
        if (ObjectUtils.isEmpty(sessionId)){
            throw new CustomException("SessionId is null");
        }
        try {


            Map<String, String> map = new HashMap() {{

                put("sessionId", sessionId);
                put("created_by", getCurrentUserName());

                if (systemServerId != null) {
                    SystemConfiguration systemConfiguration = systemConfigurationService.findById(systemServerId);
                    switch (endpointUrl) {
                        case "image" -> put("quality", systemConfiguration.getImageQuality());
                        case "pdf" -> put("quality", Optional.ofNullable(systemConfiguration.getPdfQuality()).orElse(""));
                        case "video" -> put("quality", systemConfiguration.getVideoQuality());
                        default -> throw new CustomException("Invalid file type: " + endpointUrl);
                    }
                    put("compressFolder", systemConfiguration.getRootPath() + fileDirectory);
                    put("backupFolder", systemConfiguration.getRootPath() + backUpDirectory);
                    put("username", systemConfiguration.getServerUserName());
                    put("password", systemConfiguration.getServerPassword());
                    put("serverId", systemConfiguration.getServerIp());
                    String excelFolderPath = SystemUtils.getOSPath() + serverExcelPath + sessionId;
                    FileSaver.createFolderIfNotExist(excelFolderPath);
                    put("excel_path", excelFolderPath);

                }

            }};

            log.info("Map::{}", map);

            URI uri = new URI(createServerCompressUri(endpointUrl));

            Object response = webClient.post().uri(uri).contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(map))
                    .retrieve()
                    .onStatus(a -> a.isError(), response1 -> response1.bodyToMono(Map.class)
                            .flatMap(errorBody -> Mono.error(new CustomException((String) errorBody.get("data")))))
                    .bodyToMono(Object.class)
//                    .doOnNext(success -> this.saveCompressionHistory(currentUsername,String.valueOf(map.get("sessionId")),endpointUrl))
                    .block();

            return this.saveCompressionHistory(currentUsername,String.valueOf(map.get("sessionId")),endpointUrl);

//            return response;
        }catch (CustomException e) {
            e.printStackTrace();
            log.error("Error::{}", e.getMessage());

            throw new CustomException(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error::{}", e.getMessage());

            throw new CustomException("can't compress files");
        }
    }

    private String createServerCompressUri(String fileType) {
        StringBuilder uri = new StringBuilder(this.baseUri);
        switch (fileType) {
            case "image" -> {
                return uri.append("/image/compress").toString();
            }

            case "pdf" -> {
                return uri.append("/pdf/compress").toString();
            }
            case "video" -> {
                return uri.append("/video/compress").toString();
            }
            default -> throw new CustomException("Invalid file type: " + fileType);
        }

    }

    private CompressionHistory saveCompressionHistory(String userName, String sessionId, String fileType){
        CompressionHistory compressionHistory = new CompressionHistory();
        compressionHistory.setStatus(HistoryStatus.STARTED);
        compressionHistory.setCompressedByUser(getCurrentUserName());
        compressionHistory.setSessionId(sessionId);
        compressionHistory.setFileType(fileType);

        return compressionHistoryService.save(compressionHistory);
    }

    @Override
    public String compressPdf(InputStream src, String dest) {

        try {
            PdfReader reader = new PdfReader(src);
            int n = reader.getXrefSize();
            PdfObject object;
            PRStream stream;
            int name = 0;
            // Look for image and manipulate image stream
            for (int i = 0; i < n; i++) {
                object = reader.getPdfObject(i);
                if (object == null || !object.isStream())
                    continue;
                stream = (PRStream) object;
                if (!PdfName.IMAGE.equals(stream.getAsName(PdfName.SUBTYPE)))
                    continue;
                if (!PdfName.DCTDECODE.equals(stream.getAsName(PdfName.FILTER)))
                    continue;
                PdfImageObject image = new PdfImageObject(stream);

                BufferedImage bi = image.getBufferedImage();
                if (bi == null)
                    continue;
                int width = (int) (bi.getWidth() * FACTOR);
                int height = (int) (bi.getHeight() * FACTOR);
                if (width <= 0 || height <= 0)
                    continue;
                BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                AffineTransform at = AffineTransform.getScaleInstance(FACTOR, FACTOR);
                Graphics2D g = img.createGraphics();
                g.drawRenderedImage(bi, at);
                ByteArrayOutputStream imgBytes = new ByteArrayOutputStream();

                ImageIO.write(img, "JPG", imgBytes);
                stream.clear();
                stream.setData(imgBytes.toByteArray(), false, PdfStream.BEST_COMPRESSION);
                stream.put(PdfName.TYPE, PdfName.XOBJECT);
                stream.put(PdfName.SUBTYPE, PdfName.IMAGE);
                stream.put(PdfName.FILTER, PdfName.DCTDECODE);
                stream.put(PdfName.WIDTH, new PdfNumber(width));
                stream.put(PdfName.HEIGHT, new PdfNumber(height));
                stream.put(PdfName.BITSPERCOMPONENT, new PdfNumber(8));
                stream.put(PdfName.COLORSPACE, PdfName.DEVICERGB);


            }
            reader.removeUnusedObjects();
            // Save altered PDF
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
            stamper.setFullCompression();
            stamper.close();
            reader.close();

            return dest;
        } catch (Exception e) {
            e.printStackTrace();

            return String.format("error saving file %s, Error: %s", dest, e.getMessage());

        }
    }

    private int pdfCompressionLevel(int level) {
        if (level == Integer.parseInt(null))
            level = 9;


        switch (level) {
            case 0:
                return PRStream.NO_COMPRESSION;
            case 1:
            case 2:
            case 3:
                return PRStream.DEFAULT_COMPRESSION;
            case 4:
            case 5:
            case 6:
                return PRStream.BEST_SPEED;
            default: /** 7,8,9 **/
                return PRStream.BEST_COMPRESSION;
        }
    }


    @Override
    public List<SavedFileResponseDto> compressSystemVideo(List<MultipartFile> files, String quality) {
        final String ERROR = "Error compressing file";

        Map<String, Double> originalFileSize = storeOriginalFileSize(files);


        String sessionForDirectory = Long.toString(System.currentTimeMillis());
        List<SavedFileResponseDto> savedFileResponse = new ArrayList<>();

        files.parallelStream().forEach((file) -> {
            try {
                FileSaver.uploadFile(file.getBytes(), SAVED_FOLDER + "/" + sessionForDirectory, file.getOriginalFilename());

            } catch (Exception e) {
                e.printStackTrace();
                throw new CustomException(ERROR);
            }

        });

        try {
            URI uri = new URI(baseUri + ApiEndpoints.COMPRESS_VIDEO);

            Map<String, String> directory = new HashMap() {{
                put("folderPath", SystemUtils.getOSPath().replace("\\", "/") + "images/compressed/" + sessionForDirectory);
                put("quality", quality);

            }};

            log.info("generated map::{}", directory);

            CompressedVideoResponseDto response = webClient
                    .post()
                    .uri(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(directory))
                    .retrieve()

                    .onStatus(a -> a.isError(), response1 -> response1.bodyToMono(CompressedVideoResponseDto.class)
                            .flatMap(errorBody -> Mono.error(new CustomException(errorBody.getMessage()))))
                    .bodyToMono(CompressedVideoResponseDto.class).block();

            if (!ObjectUtils.isEmpty(response)) {
                if (response.getStatus().equalsIgnoreCase("success")) {

                    File compressedFolder = new File(directory.get("folderPath"));
                    if (compressedFolder.exists()) {
                        for (File f : Objects.requireNonNull(compressedFolder.listFiles())) {
                            if (f.isFile()) {
                                StringBuilder s = new StringBuilder(f.getAbsolutePath());

                                SavedFileResponseDto savedFileResponseDto = new SavedFileResponseDto();
                                savedFileResponseDto.setFilePath(s.toString());
                                savedFileResponseDto.setName(f.getName());


//                                String compressSized = SystemUtils.getSizeInMbFromBytes(f.length());
                                long compressSized = f.length();
                                savedFileResponseDto.setCompressedSize(compressSized);
                                savedFileResponseDto.setSize(originalFileSize.get(f.getName()));
                                savedFileResponseDto.setSessionId(sessionForDirectory);
                                savedFileResponse.add(savedFileResponseDto);


                            }
                        }
                    }

                }
            }
            return savedFileResponse;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(e.getMessage());
        }

    }

    @Override
    public List<SavedFileResponseDto> compressSystem(List<MultipartFile> files, String quality, String type) {
        UserConfiguration userConfiguration = this.userService.authenticatedUser().getUserConfiguration();
        if (
                !((userConfiguration.isImage() && type.equalsIgnoreCase("image"))
                        || (userConfiguration.isPdf() && type.equalsIgnoreCase("pdf"))
                        || (userConfiguration.isMovie() && type.equalsIgnoreCase("video"))))
        {
            throw new CustomException("Unauthorized Access: "+type);
        }
        final String ERROR = "Error compressing file";

        Map<String, Double> originalFileSize = storeOriginalFileSize(files);


        String sessionForDirectory = Long.toString(System.currentTimeMillis());
        this.saveQuickCompressionReportHistory(files, type,quality,sessionForDirectory);

        List<SavedFileResponseDto> savedFileResponse = new ArrayList<>();

        files.parallelStream().forEach((file) -> {
            try {
                FileSaver.uploadFile(file.getBytes(), SAVED_FOLDER + "/" + sessionForDirectory, file.getOriginalFilename());

            } catch (Exception e) {
                e.printStackTrace();
                throw new CustomException(ERROR);
            }

        });

        try {
            URI uri;
            if (type.equalsIgnoreCase( "pdf")) {
                uri = new URI(baseUri + "/individual/pdf/compress");
            }else if(type.equalsIgnoreCase("image")){
                uri = new URI(baseUri + "/individual/image/compress");
            } else {
                uri = new URI(baseUri + ApiEndpoints.COMPRESS_VIDEO);
            }

            Map<String, String> directory = new HashMap() {{
                put("folderPath", SystemUtils.getOSPath().replace("\\", "/") + "images/compressed/" + sessionForDirectory);
                put("quality", quality);
            }};

            log.info("generated map::{}", directory);

            CompressedVideoResponseDto response = webClient
                    .post()
                    .uri(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(directory))
                    .retrieve()

                    .onStatus(a -> a.isError(), response1 -> response1.bodyToMono(CompressedVideoResponseDto.class)
                            .flatMap(errorBody -> Mono.error(new CustomException(errorBody.getMessage()))))
                    .bodyToMono(CompressedVideoResponseDto.class).block();

            if (!ObjectUtils.isEmpty(response)) {
                if (response.getStatus().equalsIgnoreCase("success")) {

                    File compressedFolder = new File(directory.get("folderPath"));
                    if (compressedFolder.exists()) {
                        for (File f : Objects.requireNonNull(compressedFolder.listFiles())) {
                            if (f.isFile()) {
                                StringBuilder s = new StringBuilder(f.getAbsolutePath());

                                SavedFileResponseDto savedFileResponseDto = new SavedFileResponseDto();
                                savedFileResponseDto.setFilePath(s.toString());
                                savedFileResponseDto.setName(f.getName());


//                                String compressSized = SystemUtils.getSizeInMbFromBytes(f.length());
                                long compressSized = f.length();
                                savedFileResponseDto.setCompressedSize(compressSized);
                                savedFileResponseDto.setSize(originalFileSize.get(f.getName()));
                                savedFileResponseDto.setSessionId(sessionForDirectory);
                                savedFileResponse.add(savedFileResponseDto);

                                /** Update compressed file history **/
                                QuickCompressionReports quickCompressionReports =
                                        quickCompressionReportsService.findBySessionIdAndFilename(sessionForDirectory,f.getName()).
                                                orElse(new QuickCompressionReports());

                                if (!quickCompressionReports.isNew()){
                                    quickCompressionReports.setCompressedSize(String.valueOf(compressSized));
                                    quickCompressionReports.setCompletedAt(new Date());
                                    quickCompressionReports.setStatus(HistoryStatus.COMPLETED);
                                    quickCompressionReports.setFilePath(s.toString());
                                    quickCompressionReportsService.save(quickCompressionReports);
                                }

                            }
                        }
                    }

                }
            }
            return savedFileResponse;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(e.getMessage());
        }

    }

    @Override
    public List<SavedFileResponseDto> compressSystemForApiUser(List<MultipartFile> files, String quality, String type, String email) {
        UserConfiguration userConfiguration = this.userService.findByEmail(email).getUserConfiguration();
        if (
                !((userConfiguration.isImage() && type.equalsIgnoreCase("image"))
                        || (userConfiguration.isPdf() && type.equalsIgnoreCase("pdf"))
                        || (userConfiguration.isMovie() && type.equalsIgnoreCase("video"))))
        {
            throw new CustomException("Unauthorized Access: "+type);
        }
        final String ERROR = "Error compressing file";

        Map<String, Double> originalFileSize = storeOriginalFileSize(files);


        String sessionForDirectory = Long.toString(System.currentTimeMillis());

        List<SavedFileResponseDto> savedFileResponse = new ArrayList<>();

        files.parallelStream().forEach((file) -> {
            try {
                FileSaver.uploadFile(file.getBytes(), SAVED_FOLDER + "/" + sessionForDirectory, file.getOriginalFilename());

            } catch (Exception e) {
                e.printStackTrace();
                throw new CustomException(ERROR);
            }

        });

        try {
            URI uri;
            if (type.equalsIgnoreCase( "pdf")) {
                uri = new URI(baseUri + "/individual/pdf/compress");
                quality = "default";
            }else if(type.equalsIgnoreCase("image")){
                uri = new URI(baseUri + "/individual/image/compress");
            } else {
                uri = new URI(baseUri + ApiEndpoints.COMPRESS_VIDEO);
            }

            String finalQuality = quality;
            Map<String, String> directory = new HashMap() {{
                put("folderPath", SystemUtils.getOSPath().replace("\\", "/") + "images/compressed/" + sessionForDirectory);
                put("quality", finalQuality);
            }};

            log.info("generated map::{}", directory);

            CompressedVideoResponseDto response = webClient
                    .post()
                    .uri(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(directory))
                    .retrieve()

                    .onStatus(a -> a.isError(), response1 -> response1.bodyToMono(CompressedVideoResponseDto.class)
                            .flatMap(errorBody -> Mono.error(new CustomException(errorBody.getMessage()))))
                    .bodyToMono(CompressedVideoResponseDto.class).block();

            if (!ObjectUtils.isEmpty(response)) {
                if (response.getStatus().equalsIgnoreCase("success")) {

                    File compressedFolder = new File(directory.get("folderPath"));
                    if (compressedFolder.exists()) {
                        for (File f : Objects.requireNonNull(compressedFolder.listFiles())) {
                            if (f.isFile()) {
                                StringBuilder s = new StringBuilder(f.getAbsolutePath());

                                SavedFileResponseDto savedFileResponseDto = new SavedFileResponseDto();
                                savedFileResponseDto.setFilePath(s.toString());
                                savedFileResponseDto.setName(f.getName());


//                                String compressSized = SystemUtils.getSizeInMbFromBytes(f.length());
                                long compressSized = f.length();
                                savedFileResponseDto.setCompressedSize(compressSized);
                                savedFileResponseDto.setSize(originalFileSize.get(f.getName()));
                                savedFileResponseDto.setSessionId(sessionForDirectory);
                                savedFileResponse.add(savedFileResponseDto);

                                /** Update compressed file history **/
                                QuickCompressionReports quickCompressionReports =
                                        quickCompressionReportsService.findBySessionIdAndFilename(sessionForDirectory,f.getName()).
                                                orElse(new QuickCompressionReports());

                                if (!quickCompressionReports.isNew()){
                                    quickCompressionReports.setCompressedSize(String.valueOf(compressSized));
                                    quickCompressionReports.setCompletedAt(new Date());
                                    quickCompressionReports.setStatus(HistoryStatus.COMPLETED);
                                    quickCompressionReports.setFilePath(s.toString());
                                    quickCompressionReportsService.save(quickCompressionReports);
                                }

                            }
                        }
                    }

                }
            }
            this.saveQuickCompressionReportHistoryForApiUser(files, type,quality,sessionForDirectory, email);
            return savedFileResponse;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(e.getMessage());
        }

    }

    @Override
    public Object getAllReports(String username) {
        /**
         Map<String, String> body = new HashMap<>();
         body.put("username", username);
         Map response = webClient
         .post()
         .uri(baseUri + "/reports/all")
         .header("Content-Type", "application/json")
         .bodyValue(body)
         .retrieve()
         .bodyToMono(Map.class).block();
         **/

        return compressionHistoryService.findAllByUserName(username);

    }


    private Map<String, Double> storeOriginalFileSize(List<MultipartFile> files) {
        Map<String, Double> originalFileSize = new HashMap<>();
        for (MultipartFile multipartFile : files) {
            try {
//                originalFileSize.put(multipartFile.getOriginalFilename(), SystemUtils.getSizeInMbFromBytes(multipartFile.getBytes()));
                originalFileSize.put(multipartFile.getOriginalFilename(), (double) multipartFile.getBytes().length);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return originalFileSize;
    }


    private void saveQuickCompressionReportHistory(List<MultipartFile> files, String fileType,String quality, String sessionId){
        List<QuickCompressionReports> compressionReportsList = new ArrayList<>();
        Map<String, Double> originalFileSize = storeOriginalFileSize(files);

        if (!files.isEmpty()){
            files.forEach( file -> {
                QuickCompressionReports quickCompressionReports = new QuickCompressionReports();
                quickCompressionReports.setQuality(quality);
                quickCompressionReports.setSessionId(sessionId);
                quickCompressionReports.setFileType(fileType);
                quickCompressionReports.setFileName(file.getOriginalFilename());
                quickCompressionReports.setCompressedByUser(getCurrentUserName());
                quickCompressionReports.setStatus(HistoryStatus.STARTED);
                quickCompressionReports.setOriginalSize(String.valueOf(originalFileSize.get(file.getOriginalFilename())));
                compressionReportsList.add(quickCompressionReports);
            });
        }
        if (!compressionReportsList.isEmpty()) {
            quickCompressionReportsService.saveAll(compressionReportsList);
        }
    }

    private String getCurrentUserName() {
        String email = userService.currentUserEmail();
        return email;
    }

    private void saveQuickCompressionReportHistoryForApiUser(List<MultipartFile> files, String fileType,String quality, String sessionId, String email){
        List<QuickCompressionReports> compressionReportsList = new ArrayList<>();
        Map<String, Double> originalFileSize = storeOriginalFileSize(files);

        if (!files.isEmpty()){
            files.forEach( file -> {
                QuickCompressionReports quickCompressionReports = new QuickCompressionReports();
                quickCompressionReports.setQuality(quality);
                quickCompressionReports.setSessionId(sessionId);
                quickCompressionReports.setFileType(fileType);
                quickCompressionReports.setFileName(file.getOriginalFilename());
                quickCompressionReports.setCompressedByUser(email);
                quickCompressionReports.setStatus(HistoryStatus.STARTED);
                quickCompressionReports.setOriginalSize(String.valueOf(originalFileSize.get(file.getOriginalFilename())));
                compressionReportsList.add(quickCompressionReports);
            });
        }
        if (!compressionReportsList.isEmpty()) {
            quickCompressionReportsService.saveAll(compressionReportsList);
        }
    }

}
