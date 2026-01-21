package com.sb.file.compressor.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Himal Rai on 1/24/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */

@Data
public class ChangePasswordRequestDto {

    @NotNull(message = "old password cannot be null")
    @NotEmpty(message = "old password cannot be empty")
    private String oldPassword;

    @NotNull(message = "new password cannot be null")
    @NotEmpty(message = "new password cannot be empty")
    private String newPassword;

    @NotNull(message = "email cannot be null")
    @NotEmpty(message = "email cannot be empty")
    private String email;
}
