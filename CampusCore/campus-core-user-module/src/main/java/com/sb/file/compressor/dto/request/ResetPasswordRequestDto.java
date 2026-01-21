package com.sb.file.compressor.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Himal Rai on 3/3/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */
@Data
public class ResetPasswordRequestDto {
    @NotNull(message = "email cannot be null")
    @NotEmpty(message = "email cannot be empty")
    private String email;
    @NotNull(message = "password cannot be null")
    @NotEmpty(message = "password cannot be empty")
    private String password;
}
