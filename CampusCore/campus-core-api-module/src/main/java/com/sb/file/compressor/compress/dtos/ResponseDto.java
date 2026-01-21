package com.sb.file.compressor.compress.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Himal Rai on 1/21/2024
 * Sb Solutions Nepal pvt.ltd
 * Project fileCompressorPocBackend.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto {
    private List<compressed_files> compressed_files;
    private boolean isImage;
    private boolean status;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class compressed_files {
        private String data;
        private String file_name;

    }
}
