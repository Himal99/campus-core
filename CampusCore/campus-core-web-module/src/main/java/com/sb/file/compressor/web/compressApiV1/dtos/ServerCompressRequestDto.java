package com.sb.file.compressor.web.compressApiV1.dtos;

import lombok.Data;

/**
 * @author Himal Rai on 1/30/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */

@Data
public class ServerCompressRequestDto {
    private String fileDirectory;
    private String backUpDirectory;
    private String endpointUrl;
    private Long serverIp;
    private String currentUsername;
    private String sessionId;
}
