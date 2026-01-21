package com.sb.file.compressor.controller;

import com.sb.file.compressor.auth.config.RequiredPermission;
import com.sb.file.compressor.core.exception.ApiResponse;
import com.sb.file.compressor.core.exception.ResponseModel;
import com.sb.file.compressor.entity.CompressActivityLog;
import com.sb.file.compressor.entity.TokenGenerationLog;
import com.sb.file.compressor.service.contract.TokenGenerationLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping( TokenGenerationLogControllerApis.ROOT) // Base URL for the controller
public class TokenGenerationLogController {

    private final TokenGenerationLogService tokenGenerationLogService;

    public TokenGenerationLogController(TokenGenerationLogService tokenGenerationLogService) {
        this.tokenGenerationLogService = tokenGenerationLogService;
    }

    // Endpoint to save token log
    @RequiredPermission({"ADMIN","API_USER"})
    @PostMapping
    public ResponseEntity<ResponseModel> saveTokenLog(@RequestBody TokenGenerationLog tokenGenerationLog) {
        TokenGenerationLog tokenGenerationLog1 = tokenGenerationLogService.saveTokenLog(tokenGenerationLog);
        return ApiResponse.success(tokenGenerationLog1);
    }

    // Endpoint to get token log by user email
    @RequiredPermission({"ADMIN","API_USER"})
    @GetMapping(TokenGenerationLogControllerApis.GET_TOKEN_LOG_BY_USER_EMAIL)
    public ResponseEntity<ResponseModel> getByUserEmail(@PathVariable("email") String email, @RequestParam("size") int size, @RequestParam("page") int page) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<TokenGenerationLog> tokenGenerationLogs = tokenGenerationLogService.getByUserEmail(email, pageable);
        return ApiResponse.success(tokenGenerationLogs);
    }

    // Endpoint to get all token logs with pagination
    @RequiredPermission({"ADMIN","API_USER"})
    @GetMapping(TokenGenerationLogControllerApis.FIND_ALL)
    public ResponseEntity<ResponseModel> findAll() {
        try {
            return ApiResponse.success(tokenGenerationLogService.findAll());
        }catch (Exception e){
            return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }
}

