package com.sb.file.compressor.dto.request;

import lombok.Data;

/**
 * @author Himal Rai on 3/3/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */
@Data
public class ForgotPasswordRequestDto {
    private String newPassword;
    private String email;
    private String otp;
}
