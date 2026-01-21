package com.sb.file.compressor.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;

/**
 * @author Himal Rai on 1/14/2024
 * Sb Solutions Nepal pvt.ltd
 * Project sb-back-core.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ResponseModel> handleAccessDeniedException(AccessDeniedException ex){
        return ApiResponse.error(HttpStatus.FORBIDDEN,ex.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ResponseModel> handleExceptions(Exception ex) {
        return ApiResponse.error(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(value = AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ResponseModel> handleAuthExceptions(AuthenticationException ex) {
        return ApiResponse.error(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(value = CustomException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseModel> handleCustomExceptions(CustomException ex) {
        return ApiResponse.error(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseModel> handleBadCredentialsExceptions(BadCredentialsException ex) {
        return ApiResponse.error(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
}
