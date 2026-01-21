package com.sb.file.compressor.auth.config.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sb.file.compressor.core.exception.ApiResponse;
import com.sb.file.compressor.utils.AppConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Himal Rai on 1/14/2024
 * Sb Solutions Nepal pvt.ltd
 * Project sb-back-core.
 */

@Slf4j
public class AppAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    public AppAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Exception exception = (Exception) request.getAttribute(AppConstants.EXCEPTION);
        OutputStream outputStream = response.getOutputStream();
        if (exception != null) {

            objectMapper.writeValue(outputStream, ApiResponse.unAuthorizedError(HttpStatus.UNAUTHORIZED, exception.getMessage()));
        } else {

            authException.getCause();
            if (authException.getCause() != null) {
                objectMapper.writeValue(outputStream,
                        ApiResponse.unAuthorizedError(HttpStatus.UNAUTHORIZED, authException.getCause().toString() + " " + authException.getMessage())
                );
            } else {
                objectMapper.writeValue(outputStream,
                        ApiResponse.unAuthorizedError(HttpStatus.UNAUTHORIZED, authException.getMessage())
                );
            }

        }
        outputStream.flush();
    }
}
