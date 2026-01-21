package com.sb.file.compressor.web.compressApiV1;

import com.sb.file.compressor.auth.service.EncryptDecrypt;
import com.sb.file.compressor.compress.dtos.SavedFileResponseDto;
import com.sb.file.compressor.compress.service.contract.CompressApiService;
import com.sb.file.compressor.compress.service.helper.CompressServiceGateway;
import com.sb.file.compressor.core.enums.CompressStatus;
import com.sb.file.compressor.core.exception.ApiResponse;
import com.sb.file.compressor.core.exception.CustomException;
import com.sb.file.compressor.core.exception.ResponseModel;
import com.sb.file.compressor.core.utils.SystemUtils;
import com.sb.file.compressor.entity.ApiUserToken;
import com.sb.file.compressor.entity.User;
import com.sb.file.compressor.model.apiUserLinkMapper.entity.ApiUserLinkMapper;
import com.sb.file.compressor.model.apiUserLinkMapper.service.ApiUserLinkMapperService;
import com.sb.file.compressor.model.compressionHistory.service.contract.CompressionHistoryService;
import com.sb.file.compressor.service.contract.ApiUserTokenService;
import com.sb.file.compressor.service.contract.CompressActivityLogService;
import com.sb.file.compressor.service.contract.UserService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping(ApiUserCompressControllerApis.ROOT)
public class ApiUserCompressController {
    private static final Logger logger = LoggerFactory.getLogger(ApiUserCompressController.class);

    private final CompressServiceGateway serviceGateway;
    private final UserService userService;
    private final ApiUserTokenService apiUserTokenService;
    private final CompressActivityLogService compressActivityLogService;
    private final ApiUserLinkMapperService apiUserLinkMapperService;
    private final EncryptDecrypt encryptDecrypt;

    @Value("${compression.server.excel.path}")
    private String serverExcelPath;

    public ApiUserCompressController(CompressServiceGateway serviceGateway, CompressApiService compressApiService, CompressionHistoryService compressionHistoryService, UserService userService, ApiUserTokenService apiUserTokenService, CompressActivityLogService compressActivityLogService, ApiUserLinkMapperService apiUserLinkMapperService, EncryptDecrypt encryptDecrypt) {
        this.serviceGateway = serviceGateway;
        this.userService = userService;
        this.apiUserTokenService = apiUserTokenService;
        this.compressActivityLogService = compressActivityLogService;
        this.apiUserLinkMapperService = apiUserLinkMapperService;
        this.encryptDecrypt = encryptDecrypt;
    }


    @PostMapping(ApiUserCompressControllerApis.COMPRESS_FILES)
    public ResponseEntity<?> compressFiles(HttpServletRequest request, @RequestParam("files") List<MultipartFile> files,
                                           @RequestParam("quality") String quality,
                                           @RequestParam("type") String type,
                                           @RequestParam("response-type") String responseType, HttpServletResponse response) {
        String remoteAddr = request.getRemoteAddr();
        String bearerToken = request.getHeader("Authorization");
        String token = null;
        if (bearerToken != null && bearerToken.startsWith("Bearer")) {
            token = bearerToken.substring(7);
        } else { throw new BadCredentialsException("Invalid Header Value !!"); }
        Optional<ApiUserToken> userToken = apiUserTokenService.getByTokenValue(token);
        userToken.orElseThrow(() ->
                new RuntimeException("Invalid Token Provided"));
        User apiUser = userToken.get().getUser();
        String ipAddress = userToken.get().getIpAddress();
//        if (!remoteAddr.equals(ipAddress)) {
//            compressActivityLogService.saveCompressLog(apiUser.getEmail(), CompressStatus.FAIL);
//            throw new RuntimeException("Unauthorized IP Address");
//        }
        try{ performValidation(files, type);
             List<SavedFileResponseDto> savedFileResponseDtoList = serviceGateway.compressFilesForApiUser(files, quality, type, apiUser.getEmail());
             List<String> base64Files = new ArrayList<>();
             if (!ObjectUtils.isEmpty(savedFileResponseDtoList)) {
                String sessionId = savedFileResponseDtoList.get(0).getSessionId();

                 if(responseType.equalsIgnoreCase("byte")){
                     try {
                         String directoryPath = SystemUtils.getOSPath() + File.separator + "images/compressed" + File.separator + sessionId;
                         File directory = new File(directoryPath);
                         String[] fileNames = directory.list();
                         if (fileNames != null) {
                             for (String fileName : fileNames) {
                                 try (InputStream inputStream = new FileInputStream(directoryPath + "/" + fileName)) {
                                     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                     byte[] buffer = new byte[1024];
                                     int bytesRead;
                                     while ((bytesRead = inputStream.read(buffer)) != -1) {
                                         byteArrayOutputStream.write(buffer, 0, bytesRead);
                                     }
                                     byte[] imageBytes = byteArrayOutputStream.toByteArray();

                                     String base64Image = Base64.getEncoder().encodeToString(imageBytes);

                                     base64Files.add(base64Image);
                                 }
                             }
                         }
                         compressActivityLogService.saveCompressLog(apiUser.getEmail(), CompressStatus.SUCCESS);
                         return ApiResponse.success(base64Files);
                     } catch (IOException e) {
                         compressActivityLogService.saveCompressLog(apiUser.getEmail(), CompressStatus.FAIL);
                         return new ResponseEntity<>(new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
                     }
                 } else if(responseType.equalsIgnoreCase("file")){
                     try {
//                         this.downloadFile(response, sessionId);
                         String directoryPath = SystemUtils.getOSPath() + File.separator + "images/compressed" + File.separator + sessionId;
                         File directory = new File(directoryPath);
                         String[] fileNames = directory.list();
                         if (fileNames != null) {
                             HttpHeaders headers = this.getHeaders(fileNames[0]);
                             File file = new File(directoryPath +File.separator+ fileNames[0]);
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
                         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                     } catch (Exception e) {
                         compressActivityLogService.saveCompressLog(apiUser.getEmail(), CompressStatus.FAIL);
                         return new ResponseEntity<>(new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
                     }
                 } else if(responseType.equalsIgnoreCase("link")){
                     String host = request.getServerName();
                     int port = request.getServerPort();
                     String scheme = request.getScheme();
                     String link = "";
                     try {
                         String directoryPath = SystemUtils.getOSPath() + File.separator + "images/compressed" + File.separator + sessionId;
                         File directory = new File(directoryPath);
                         String[] fileNames = directory.list();
                         List<String> linkString = new ArrayList<>();
                         String baseLink= scheme+"://"+host+":"+port+"/api/v1/download/"+sessionId;

                         if (fileNames != null) {
                             Arrays.stream(fileNames).forEach(x->{
                                 linkString.add(baseLink+"/"+x);
                             });
//                             link = scheme+"://"+host+":"+port+"/api/v1/download/"+sessionId;
                         }
                         compressActivityLogService.saveCompressLog(apiUser.getEmail(), CompressStatus.SUCCESS);
                         apiUserLinkMapperService.save(new ApiUserLinkMapper()
                                 .builder()
                                         .tokenId(String.valueOf(userToken.get().getId()))
                                         .sessionId(sessionId)
                                 .build());
                         return ApiResponse.success(linkString);
                     } catch (Exception e) {
                         compressActivityLogService.saveCompressLog(apiUser.getEmail(), CompressStatus.FAIL);
                         return new ResponseEntity<>(new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
                     }
                 } else {
                     throw new CustomException("Invalid Response type! Should be byte or file or link.");
                 }
            } else {
                 compressActivityLogService.saveCompressLog(apiUser.getEmail(), CompressStatus.FAIL);
                 return new ResponseEntity<>(HttpStatus.NO_CONTENT);
             }
        }catch (Exception e){
            compressActivityLogService.saveCompressLog(apiUser.getEmail(), CompressStatus.FAIL);
            throw new RuntimeException(e.getMessage());
        }
    }

    private void performValidation(List<MultipartFile> files, String type) {
        Integer imageMax = 1;
        Integer pdfMax = 1;
        Integer videoMax = 1;
        Integer IMAGE_MAX_SIZE = 20 * 1024 * 1024; // 50 MB
        Integer PDF_MAX_SIZE = 20 * 1024 * 1024; // 50 MB
        Integer VIDEO_MAX_SIZE = 100 * 1024 * 1024; // 50 MB
        String[] imageAllowedExtensions = {"jpeg", "jpg", "png"};
        String[] pdfAllowedExtensions = {"pdf"};
        String[] videoAllowedExtensions = {"mp4","webm","mkv", "avi","mov"};

        Integer maxAllowed;
        Integer maxSize;
        String[] allowedExtensions;

        switch (type) {
            case "image":
                maxAllowed = imageMax;
                maxSize = IMAGE_MAX_SIZE;
                allowedExtensions = imageAllowedExtensions;
                break;
            case "pdf":
                maxAllowed = pdfMax;
                maxSize = PDF_MAX_SIZE;
                allowedExtensions = pdfAllowedExtensions;
                break;
            case "video":
                maxAllowed = videoMax;
                maxSize = VIDEO_MAX_SIZE;
                allowedExtensions = videoAllowedExtensions;
                break;
            default:
                throw new RuntimeException("Invalid type");
        }

        if (files.size() > maxAllowed) {
            throw new RuntimeException("Too many files. Maximum allowed: " + maxAllowed);
        }

        for (MultipartFile file : files) {
            if (file.getSize() > maxSize) {
                throw new RuntimeException("File size exceeds the maximum allowed size. Maximum allowed size: " + maxSize/(1024 * 1024) + " mb");
            }
            String fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
            if (fileExtension == null || !isExtensionAllowed(fileExtension, allowedExtensions)) {
                throw new RuntimeException("File extension not allowed: " + fileExtension);
            }
        }
    }

    private void writePart(ServletOutputStream out, Path file) throws IOException {
        String header = "\r\n--boundary\r\n" +
                "Content-Disposition: attachment; filename=\"" + file.getFileName().toString() + "\"\r\n" +
                "Content-Type: application/octet-stream\r\n\r\n";
        out.write(header.getBytes());

        try (InputStream inputStream = Files.newInputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }

    private boolean isExtensionAllowed(String extension, String[] allowedExtensions) {
        for (String allowedExtension : allowedExtensions) {
            if (allowedExtension.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

    @GetMapping(ApiUserCompressControllerApis.LINK)
    public ResponseEntity<?> downloadLink(@PathVariable("sessionId") String sessionId, @PathVariable("fileName") String fileName, HttpServletResponse response, HttpServletRequest request){
//        String bearerToken = request.getHeader("Authorization");
//        String token = null;
//        if (bearerToken != null && bearerToken.startsWith("Bearer")) {
//            token = bearerToken.substring(7);
//        } else { throw new BadCredentialsException("Invalid Header Value !!"); }
//        Optional<ApiUserToken> userToken = apiUserTokenService.getByTokenValue(token);
//        userToken.orElseThrow(() ->
//                new CustomException("Invalid Token Provided"));
//        Optional<ApiUserLinkMapper> byApiUserId = apiUserLinkMapperService.findByTokenIdAndSessionId(String.valueOf(userToken.get().getId()),sessionId);
//        byApiUserId.orElseThrow(() ->
//                new CustomException("Invalid Link"));
        try {
//            this.downloadFile(response, sessionId);
            String directoryPath = SystemUtils.getOSPath() + File.separator + "images/compressed" + File.separator + sessionId + File.separator + fileName;
            File file = new File(directoryPath);
            if (!ObjectUtils.isEmpty(file)) {
                HttpHeaders headers = this.getHeaders(file.getName());
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
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new CustomException("Unable to download fies");
        }
    }

    private void downloadFile(HttpServletResponse response, String sessionId){
        response.setContentType("multipart/mixed; boundary=boundary");
        try (ServletOutputStream out = response.getOutputStream()) {
            String directoryPath = SystemUtils.getOSPath() + File.separator + "images/compressed" + File.separator + sessionId;
            File directory = new File(directoryPath);
            String[] fileNames = directory.list();
            if (fileNames != null) {
                for (String fileName : fileNames) {
                    Path file = Path.of(directoryPath + File.separator + fileName);
                    if (Files.isReadable(file)) {
                        this.writePart(out, file);
                    } else {
                        throw new RuntimeException("File not found or not readable: " + fileName);
                    }
                }
                out.write(("--boundary--").getBytes());
                out.flush();
            }
        }catch (IOException e){
            throw new CustomException("Error in file download");
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
}
