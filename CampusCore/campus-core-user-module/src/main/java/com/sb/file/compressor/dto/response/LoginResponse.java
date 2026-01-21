package com.sb.file.compressor.dto.response;

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
public class LoginResponse {
    private String token;
    private String message;


    private String userName;

    private String roleName;
    private String email;
    private Boolean serverCompressor;

    /**
     * constructor for refresh token
     */
    public LoginResponse(String token, String message) {
        this.token = token;
        this.message = message;
    }

    private Boolean isImageCompression;
    private Boolean isPdfCompression;
    private Boolean isVideoCompression;
    private Boolean isPasswordChanged;

    public LoginResponse(String jwtToken, String s, String email) {
        this.token = jwtToken;
        this.message = s;
        this.email = email;
    }
}
