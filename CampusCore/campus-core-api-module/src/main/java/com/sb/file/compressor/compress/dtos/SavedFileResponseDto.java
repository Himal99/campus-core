package com.sb.file.compressor.compress.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Himal Rai on 1/21/2024
 * Sb Solutions Nepal pvt.ltd
 * Project fileCompressorPocBackend.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SavedFileResponseDto {
    private String name;
    private String filePath;
    private Object compressedSize;
    private double size;
    private String sessionId;
}
