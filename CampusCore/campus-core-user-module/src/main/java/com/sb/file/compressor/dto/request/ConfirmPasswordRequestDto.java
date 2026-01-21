package com.sb.file.compressor.dto.request;

import lombok.Data;

/**
 * @author Himal Rai on 2/5/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */
@Data
public class ConfirmPasswordRequestDto {
    private String email;
    private String password;
}
