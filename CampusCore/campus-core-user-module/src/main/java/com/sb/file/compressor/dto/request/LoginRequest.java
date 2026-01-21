package com.sb.file.compressor.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Himal Rai on 1/14/2024
 * Sb Solutions Nepal pvt.ltd
 * Project sb-back-core.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotNull(message = "Email can't be null")
    @NotEmpty(message = "Email can't be empty")
    private String email;

    @NotNull(message = "Password can't be null")
    @NotEmpty(message = "Password can't be empty")
    private String password;

}
