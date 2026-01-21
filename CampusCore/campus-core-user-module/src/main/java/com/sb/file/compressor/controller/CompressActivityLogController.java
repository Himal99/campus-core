package com.sb.file.compressor.controller;

import com.sb.file.compressor.auth.config.RequiredPermission;
import com.sb.file.compressor.core.exception.ApiResponse;
import com.sb.file.compressor.core.exception.ResponseModel;
import com.sb.file.compressor.entity.CompressActivityLog;
import com.sb.file.compressor.service.contract.CompressActivityLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(CompressActivityLogControllerApis.ROOT)
public class CompressActivityLogController {


    private final CompressActivityLogService compressActivityLogService;


    public CompressActivityLogController(CompressActivityLogService compressActivityLogService) {
        this.compressActivityLogService = compressActivityLogService;
    }

    @RequiredPermission({"ADMIN", "API_USER"})
    @GetMapping(CompressActivityLogControllerApis.GET_COMPRESS_LOG_BY_USER_EMAIL)
    public ResponseEntity<ResponseModel> findByUserEmail(@PathVariable("email") String email,@RequestParam("size") int size, @RequestParam("page") int page){
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<CompressActivityLog> byUserEmail = compressActivityLogService.getByUserEmail(email, pageable);
        return ApiResponse.success(byUserEmail);
    }

    @RequiredPermission({"ADMIN", "API_USER"})
    @GetMapping(CompressActivityLogControllerApis.PAGEABLE)
    public ResponseEntity<ResponseModel> findAllPageable(@RequestParam("size") int size, @RequestParam("page") int page){
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<CompressActivityLog> allPageable = compressActivityLogService.findAllPageable(pageable);
        return ApiResponse.success(allPageable);
    }


}
