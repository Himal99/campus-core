package com.sb.file.compressor.web.systemConfigurationApiV1;

import com.sb.file.compressor.auth.config.RequiredPermission;
import com.sb.file.compressor.core.exception.ApiResponse;
import com.sb.file.compressor.core.utils.PaginationUtils;
import com.sb.file.compressor.model.systemConfig.entity.SystemConfiguration;
import com.sb.file.compressor.model.systemConfig.service.SystemConfigurationService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.sb.file.compressor.web.systemConfigurationApiV1.SystemConfigurationControllerApis.*;

/**
 * @author Himal Rai on 1/31/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */
@RestController
@RequestMapping(SYSTEM_CONFIGURATION_ROOT)
@Slf4j
public class SystemConfigurationController {

    private final SystemConfigurationService systemConfigurationService;

    public SystemConfigurationController(SystemConfigurationService systemConfigurationService) {
        this.systemConfigurationService = systemConfigurationService;
    }

    @RequiredPermission({"ADMIN", "USER"})
    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody SystemConfiguration configuration) {
        return ApiResponse.success(systemConfigurationService.save(configuration));
    }

    @RequiredPermission({"ADMIN", "USER"})
    @GetMapping(LIST)
    public ResponseEntity<?> getALl() {
        return ApiResponse.success(systemConfigurationService.findAll());
    }

    @RequiredPermission({"ADMIN", "USER"})
    @GetMapping(ID)
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {

        return ApiResponse.success(systemConfigurationService.findById(id));
    }

    @RequiredPermission({"ADMIN", "USER"})
    @PostMapping(PAGEABLE_LIST_WITH_SEARCH_OBJECT)
    public ResponseEntity<?> findAllByPageableWithSearchObject(@RequestParam("size") int size, @RequestParam("page") int page,
                                                               @RequestBody Map<String, String> body) {
        try {
            if (body.containsKey("name")) {
                String name = body.get("name");
                Page<SystemConfiguration> systemConfigurations = systemConfigurationService.findAllByPaginationWithSearchObject(name, PaginationUtils.pageable(page, size));
                return ApiResponse.success(systemConfigurations);
            } else {
                Page<SystemConfiguration> systemConfigurations = systemConfigurationService.findAllByPaginationWithSearchObject(null, PaginationUtils.pageable(page, size));
                return ApiResponse.success(systemConfigurations);
            }
        } catch (Exception e) {
            return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequiredPermission({"ADMIN", "USER"})
    @GetMapping(value = DELETE_BY_ID)
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
        try {
            return ApiResponse.success(systemConfigurationService.deleteById(id));
        } catch (Exception e) {
            return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
