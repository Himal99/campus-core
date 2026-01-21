package com.sb.file.compressor.web.quickCompressConfigurationApiV1;

import com.sb.file.compressor.auth.config.RequiredPermission;
import com.sb.file.compressor.core.exception.ApiResponse;
import com.sb.file.compressor.model.quickCompressConfiguration.entity.QuickCompressConfiguration;
import com.sb.file.compressor.model.quickCompressConfiguration.service.contract.QuickCompressConfigurationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sb.file.compressor.web.quickCompressConfigurationApiV1.QuickCompressConfigurationControllerApis.*;

/**
 * @author Himal Rai on 2/21/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */
@RestController
@RequestMapping(QUICK_COMPRESS_CONFIGURATION_ROOT)
public class QuickCompressConfigurationController {

    private final QuickCompressConfigurationService quickCompressConfigurationService;

    public QuickCompressConfigurationController(QuickCompressConfigurationService quickCompressConfigurationService) {
        this.quickCompressConfigurationService = quickCompressConfigurationService;
    }
    @RequiredPermission({"ADMIN", "USER"})
    @PostMapping
    public ResponseEntity<?> saveConfiguration(@RequestBody QuickCompressConfiguration configuration)
    {
        QuickCompressConfiguration savedConfiguration = quickCompressConfigurationService.save(configuration);
        return ApiResponse.success(savedConfiguration);
    }

    @RequiredPermission({"ADMIN", "USER"})
    @GetMapping(ID)
    public ResponseEntity<?> getById(@PathVariable Long id)
    {
        QuickCompressConfiguration fetchedConfiguration = quickCompressConfigurationService.findOne(id);
        return ApiResponse.success(fetchedConfiguration);
    }

    @RequiredPermission({"ADMIN", "USER"})
    @GetMapping(LIST)
    public ResponseEntity<?> getAllConfig()
    {
        List<QuickCompressConfiguration> fetchedConfiguration = quickCompressConfigurationService.findAll();
        return ApiResponse.success(fetchedConfiguration);
    }


}
