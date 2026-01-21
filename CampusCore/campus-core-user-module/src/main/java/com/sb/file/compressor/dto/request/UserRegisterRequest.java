package com.sb.file.compressor.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @author Himal Rai on 1/14/2024
 * Sb Solutions Nepal pvt.ltd
 * Project sb-back-core.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequest {

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

    @NotNull(message = "Password can't be null")
    @NotEmpty(message = "Password can't be empty")

    private String password;
    private Set<String> role;

    private String phoneNumber;

    private Boolean serverCompressor;
}
