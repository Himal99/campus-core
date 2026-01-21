package com.sb.file.compressor.web.compressApiV1;

import com.sb.file.compressor.auth.config.RequiredPermission;
import com.sb.file.compressor.controller.TokenGenerationLogControllerApis;
import com.sb.file.compressor.core.exception.ApiResponse;
import com.sb.file.compressor.core.exception.ResponseModel;
import com.sb.file.compressor.entity.TokenGenerationLog;
import com.sb.file.compressor.model.quickCompressReports.entity.QuickCompressionReports;
import com.sb.file.compressor.model.quickCompressReports.service.contract.QuickCompressionReportsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/quick-compression-report")
public class QuickCompressionReportController {
    private static final Logger logger = LoggerFactory.getLogger(ApiUserCompressController.class);

    private final QuickCompressionReportsService quickCompressionReportsService;

    public QuickCompressionReportController(QuickCompressionReportsService quickCompressionReportsService) {
        this.quickCompressionReportsService = quickCompressionReportsService;
    }

    @RequiredPermission({"ADMIN"})
    @GetMapping("/list")
    public ResponseEntity<ResponseModel> getByUserEmail(@RequestParam("size") int size, @RequestParam("page") int page) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<QuickCompressionReports> quickCompressionReports = quickCompressionReportsService.findAllPage(pageable);
        return ApiResponse.success(quickCompressionReports);
    }
}
