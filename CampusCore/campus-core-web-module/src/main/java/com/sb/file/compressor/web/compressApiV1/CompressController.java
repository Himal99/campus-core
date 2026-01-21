package com.sb.file.compressor.web.compressApiV1;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sb.file.compressor.auth.config.RequiredPermission;
import com.sb.file.compressor.compress.dtos.SavedFileResponseDto;
import com.sb.file.compressor.compress.service.contract.CompressApiService;
import com.sb.file.compressor.compress.service.helper.CompressServiceGateway;
import com.sb.file.compressor.core.email.MailSenderService;
import com.sb.file.compressor.core.exception.ApiResponse;
import com.sb.file.compressor.core.exception.CustomException;
import com.sb.file.compressor.core.exception.ResponseModel;
import com.sb.file.compressor.core.utils.PaginationUtils;
import com.sb.file.compressor.core.utils.SystemUtils;
import com.sb.file.compressor.model.compressionHistory.entity.CompressionHistory;
import com.sb.file.compressor.model.compressionHistory.enums.HistoryStatus;
import com.sb.file.compressor.model.compressionHistory.service.contract.CompressionHistoryService;
import com.sb.file.compressor.service.contract.UserService;
import com.sb.file.compressor.web.LogMessage;
import com.sb.file.compressor.web.RealTimeLogService;
import com.sb.file.compressor.web.compressApiV1.dtos.ServerCompressRequestDto;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.sb.file.compressor.web.compressApiV1.CompressControllerApis.CALL_BACK;
import static com.sb.file.compressor.web.compressApiV1.CompressControllerApis.LOG_REPORTS;


/**
 * @author Himal Rai on 1/24/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */
@RestController
@RequestMapping(CompressControllerApis.COMPRESS_ROOT)
public class CompressController {
    private static final Logger logger = LoggerFactory.getLogger(CompressController.class);

    private final CompressServiceGateway serviceGateway;
    private final CompressApiService compressApiService;
    private final CompressionHistoryService compressionHistoryService;
    private ObjectMapper mapper;
    private final MailSenderService mailSenderService;

    private final RealTimeLogService realTimeLogService;

    private final UserService userService;
    //    private final SimpMessagingTemplate simpMessagingTemplate;
    @Value("${compression.server.excel.path}")
    private String serverExcelPath;


    public CompressController(CompressServiceGateway compressApiService, CompressApiService compressApiService1, CompressionHistoryService compressionHistoryService,
                              MailSenderService mailSenderService, RealTimeLogService realTimeLogService, UserService userService) {
        this.serviceGateway = compressApiService;
        this.compressApiService = compressApiService1;
        this.compressionHistoryService = compressionHistoryService;
        this.mailSenderService = mailSenderService;
        this.realTimeLogService = realTimeLogService;
        this.userService = userService;
//        this.simpMessagingTemplate = simpMessagingTemplate;

        this.mapper = new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .setSerializationInclusion(JsonInclude.Include.NON_EMPTY).disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @RequiredPermission({"ADMIN", "USER"})
    @PostMapping(CompressControllerApis.COMPRESS_FILES)
    public ResponseEntity<?> compressFiles(@RequestParam("files") List<MultipartFile> files,
                                           @RequestParam("quality") String quality,
                                           @RequestParam("type") String type) {
        logger.info("ok::{}", files.get(0).getOriginalFilename());

        try {
            List<SavedFileResponseDto> savedFileResponseDto = serviceGateway.compressFiles(files, quality, type);
            return ApiResponse.success(savedFileResponseDto);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequiredPermission({"ADMIN", "USER"})
    @PostMapping(CompressControllerApis.SERVER_COMPRESS)
    public ResponseEntity<?> serverCompress(@RequestBody ServerCompressRequestDto requestDto) {
        try {
            Object o = compressApiService.compressServerFile(requestDto.getFileDirectory(),
                    requestDto.getBackUpDirectory(), requestDto.getEndpointUrl(), requestDto.getServerIp(), requestDto.getCurrentUsername(), requestDto.getSessionId());
            return ApiResponse.success(o);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @RequiredPermission({"ADMIN", "USER"})
    @PostMapping(CompressControllerApis.COMPRESS_NOTIFICATION_API)
    public ResponseEntity<?> getCompressNotification(@RequestBody Map<String, Object> body) {

        return ApiResponse.success("success");
    }

    @RequiredPermission({"ADMIN", "USER"})
    @PostMapping(CompressControllerApis.COMPRESS_VIDEO)
    public ResponseEntity<?> compressSystemVideo(@RequestParam("files") List<MultipartFile> files,
                                                 @RequestParam("quality") String quality) {
        try {
            List<SavedFileResponseDto> response = compressApiService.compressSystemVideo(files, quality);
            return ApiResponse.success(response);
        } catch (Exception e) {
            if (e.getMessage().equalsIgnoreCase("Invalid or missing folderPath")) {
                return new ResponseEntity<>(new ResponseModel(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @RequiredPermission({"ADMIN", "USER"})
    @GetMapping(CompressControllerApis.DOWNLOAD_SINGLE_FILE)
    public ResponseEntity<?> downloadSingleFile(@RequestParam("fileName") String fileName,
                                                @RequestParam("sessionId") String sessionId) {

        HttpHeaders headers = this.getHeaders(fileName);
        File file = new File(SystemUtils.getOSPath() + "images\\compressed\\" + sessionId + "\\" + fileName);
        InputStreamResource inputStream = null;
        try {
            inputStream = new InputStreamResource(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok().headers(headers)
                .contentLength(-1)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(inputStream);
    }

    @RequiredPermission({"ADMIN", "USER"})
    @GetMapping("/download-log-file")
    public ResponseEntity<?> downloadLogFile(@RequestParam("sessionId") String sessionId) {
        HttpHeaders headers = this.getHeaders(sessionId);
        File file = new File(SystemUtils.getOSPath() + serverExcelPath + sessionId + "\\" + sessionId+".txt");
        InputStreamResource inputStream = null;
        try {
            inputStream = new InputStreamResource(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok().headers(headers)
                .contentLength(-1)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(inputStream);
    }

    @RequiredPermission({"ADMIN", "USER"})
    @GetMapping(CompressControllerApis.DOWNLOAD_ALL_FILE)
    public void downloadAllFile(@RequestParam("sessionId") String sessionId,
                                HttpServletResponse response) {


        String directoryPath = SystemUtils.getOSPath() + "images\\compressed\\" + sessionId;
        File directory = new File(directoryPath);
        String zipFileName = sessionId + ".zip";

        try {
            // Setting up HTTP Response for downloading
            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment;filename=\"" + zipFileName + "\"");

            // Create a zip output stream to write the zip file to the response
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(response.getOutputStream());
            ZipOutputStream zipOut = new ZipOutputStream(bufferedOutputStream);

            // List all files in the directory and add them to the zip
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    FileInputStream fis = new FileInputStream(file);
                    ZipEntry zipEntry = new ZipEntry(file.getName());
                    zipOut.putNextEntry(zipEntry);

                    byte[] bytes = new byte[1024];
                    int length;
                    while ((length = fis.read(bytes)) >= 0) {
                        zipOut.write(bytes, 0, length);
                    }
                    fis.close();
                }
            }
            zipOut.close();
            bufferedOutputStream.close();
            response.flushBuffer();
        } catch (Exception exception) {
            exception.printStackTrace();

        }

    }


    private HttpHeaders getHeaders(String fileName) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        return headers;
    }

    /**
     * give list of reports of the server compression.
     * The sessionId is maintained while compression is started with status STARTED.
     * If the status is not in COMPLETED, then show the report from the database.
     * If the status is COMPLETED, then show the report from calling the api.
     *
     * @param body required request body for getting reports.
     * @return list of reports based on sessionId.
     */

    @RequiredPermission({"ADMIN", "USER"})
    @PostMapping(LOG_REPORTS)
    public ResponseEntity<?> getReportsFromApi(@RequestParam("size") int size, @RequestParam("page") int page,
                                               @RequestBody Map<String, String> body) {
        try {
            String name = userService.currentUserEmail();
            Page<CompressionHistory> compressionHistories = compressionHistoryService.findAllByCompressedByUserWithSearchObject(name,
                    body.getOrDefault("sessionId", ""), PaginationUtils.pageable(page, size));
            return ApiResponse.success(compressionHistories);
        } catch (Exception e) {
            return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * This method is to get the response callback of the server compression based on sessionId.
     * When server compression is started, the session stage should be saved somewhere with status STARTED.
     * And when this callback api is called, that session will be finished with some log report details.
     *
     * @param body data of the server compression for the sessionId.
     * @return report of the server compression
     */
//    @RequiredPermission({"ADMIN", "USER"})
    @PostMapping(CALL_BACK)
    public ResponseEntity<ResponseModel> getCompressCallback(@RequestBody Object body) throws JsonProcessingException {
        logger.info("::: CALLBACK IS CALLED ::: ");
        this.updateCompressionHistory(body);
        try {
            logger.info("::: SENDING EMAIL. SERVER COMPRESSION FINISHED ::: ");
            this.sendSuccessMailNotification(body);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.success(body);
        }
        return ApiResponse.success(body);
    }

    private void updateCompressionHistory(Object object) throws JsonProcessingException {
        Map<String, Object> map = this.mapper.convertValue(object, Map.class);

        if (map.containsKey("sessionId")) {
            String sessionId = map.get("sessionId").toString();

            CompressionHistory compressionHistory =
                    compressionHistoryService.findBySessionId(sessionId);
            if (!compressionHistory.isNew()) {
                compressionHistory.setStatus(HistoryStatus.COMPLETED);
                compressionHistory.setHistoryDetails(mapper.writeValueAsString(object));
                compressionHistory.setCompletedAt(new Date());
                compressionHistoryService.save(compressionHistory);
            } else {
                throw new CustomException("Could not find history for session");
            }

        } else {
            throw new CustomException("Cannot parse object");
        }
    }

    private void sendSuccessMailNotification(Object object) {
        if (object != null) {
            Map<String, Object> map = this.mapper.convertValue(object, Map.class);

            String mailBody = String.format("""
                            Hi,
                            Your server compression has been completed successfully.
                            Compression details:
                            Total compressed: %d,
                            Total files: %d""",
                    (int) map.getOrDefault("total_compressed_status", 0),
                    (int) map.getOrDefault("total_files", 0));

            mailSenderService.sendSimpleMail((String) map.getOrDefault("created_by", ""), "Successfully compressed your file", mailBody);
        }
    }

    @RequiredPermission({"ADMIN", "USER"})
    @GetMapping("/download-excel")
    public ResponseEntity<?> downloadExcelOfSession(@RequestParam("sessionId") String sessionId,
                                                    HttpServletResponse response) {
        String excelFolderPath = SystemUtils.getOSPath() + serverExcelPath + sessionId;
        String normalizedPath = excelFolderPath.replace("\\", "/");
        String excelFilePath = normalizedPath + "/" + sessionId + ".xlsx";

        File file = new File(excelFilePath);

        // Ensure the file exists
        if (!file.exists()) {
            return new ResponseEntity<>(new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Excel sheet not found", null), HttpStatus.INTERNAL_SERVER_ERROR);

//            throw new CustomException("Excel sheet not found");
        }

        // Create a resource to serve
        Resource resource = new FileSystemResource(file);

        // Set the content type and attachment header
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(resource);

    }

//    @GetMapping(value = "/realtime-server-logs/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public Flux<LogMessage> notifications() {
//        Flux<LogMessage> notificationStream = this.realTimeLogService.getNotificationStream();
//        return notificationStream;
//    }

    @RequiredPermission({"ADMIN", "USER"})
    @GetMapping(value = "/get-all-server-log/{sessionId}")
    public ResponseEntity<?> getAllServerLogs(@PathVariable("sessionId") String sessionId) throws IOException {
        File file = new File(SystemUtils.getOSPath()+"/images/serverLog/"+sessionId+".txt");
        StringBuilder response = new StringBuilder();
        List<LogMessage> messages = new ArrayList<>();
        if (file.exists()){
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            if (response.length() > 0) {
                messages = mapper.readValue(response.toString(), mapper.getTypeFactory().constructType(List.class, LogMessage.class));
            }
        }
        messages = messages == null ? List.of(new LogMessage()) : messages ;
        return ApiResponse.success(HttpStatus.OK,"Success", messages);
    }

}
