package com.sb.file.compressor.dto.request;

import lombok.Data;

/**
 * @author Himal Rai on 2/22/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */

@Data
public class UserOtpRequestDto {
    private String userName;

    private String email;

    private String mobile;
}
