package com.sb.file.compressor.dto.response;

import lombok.Data;

/**
 * @author Himal Rai on 1/29/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */

@Data
public class UserResponseDto {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;

}
