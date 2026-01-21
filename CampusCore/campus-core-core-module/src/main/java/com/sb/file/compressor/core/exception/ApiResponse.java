package com.sb.file.compressor.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author Himal Rai on 1/14/2024
 * Sb Solutions Nepal pvt.ltd
 * Project sb-back-core.
 */
public class ApiResponse {
    public static ResponseEntity<ResponseModel> success(Object data) {
        return ResponseEntity.ok(new ResponseModel(HttpStatus.OK.value(), "success", data));
    }

    public static ResponseEntity<ResponseModel> success(HttpStatus status, String message, Object data) {
        return ResponseEntity.ok(new ResponseModel(status.value(), message, data));
    }

    public static ResponseEntity<ResponseModel> error(HttpStatus status, String message) {
        return new ResponseEntity<>(new ResponseModel(status.value(),
                message, null), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static ResponseEntity<ResponseModel> unAuthorizedError(HttpStatus status, String message) {
        return new ResponseEntity<>(new ResponseModel(HttpStatus.UNAUTHORIZED.value(),
                message, null), HttpStatus.UNAUTHORIZED);
    }
}
