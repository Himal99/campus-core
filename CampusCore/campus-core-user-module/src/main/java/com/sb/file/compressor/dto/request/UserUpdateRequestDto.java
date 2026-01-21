package com.sb.file.compressor.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Himal Rai on 2/29/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */

@Data
public class UserUpdateRequestDto {
    private Long id;

    @NotNull(message = "Email can't be null")
    @NotEmpty(message = "Email can't be empty")
    private String email;

    @NotNull(message = "First Name can't be null")
    @NotEmpty(message = "First Name can't be empty")
    private String firstName;

    @NotNull(message = "Last Name can't be null")
    @NotEmpty(message = "Last Name can't be empty")
    private String lastName;

    private String phoneNumber;

    private Boolean serverCompressor;
}
