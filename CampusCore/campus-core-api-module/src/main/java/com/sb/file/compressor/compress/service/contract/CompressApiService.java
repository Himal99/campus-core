package com.sb.file.compressor.compress.service.contract;

import com.sb.file.compressor.compress.dtos.SavedFileResponseDto;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * @author Himal Rai on 1/24/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */
public interface CompressApiService {
    List<SavedFileResponseDto> compressImages(MultiValueMap<String, Object> multiValueMap, String sessionId);

    Object compressServerFile(String fileDirectory, String backUpDirectory, String endpointUrl, Long systemServerId, String currentUsername, String sessionId);

    String compressPdf(InputStream src, String dest);

    List<SavedFileResponseDto> compressSystemVideo(List<MultipartFile> multiValueMap, String quality);

    List<SavedFileResponseDto> compressSystem(List<MultipartFile> files, String quality, String type);
    List<SavedFileResponseDto> compressSystemForApiUser(List<MultipartFile> files, String quality, String type, String email);

    Object getAllReports(String username);
}
