package com.sb.file.compressor.compress.service.helper;

import com.sb.file.compressor.compress.converter.MultipartFileConverter;
import com.sb.file.compressor.compress.dtos.SavedFileResponseDto;
import com.sb.file.compressor.compress.service.contract.CompressApiService;
import com.sb.file.compressor.core.exception.CustomException;
import com.sb.file.compressor.core.utils.SystemUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Himal Rai on 1/24/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */
@Component
public class CompressServiceGateway {
    private final CompressApiService compressApiService;

    public CompressServiceGateway(CompressApiService compressApiService) {
        this.compressApiService = compressApiService;
    }

    public List<SavedFileResponseDto> compressFiles(List<MultipartFile> files, String quality, String type) {
        return this.compressApiService.compressSystem(files, quality, type);
    }

    public List<SavedFileResponseDto> compressFilesForApiUser(List<MultipartFile> files, String quality, String type,String email) {
        return this.compressApiService.compressSystemForApiUser(files, quality, type,email);
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
}
