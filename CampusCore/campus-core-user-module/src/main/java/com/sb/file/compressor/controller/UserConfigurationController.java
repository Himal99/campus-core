package com.sb.file.compressor.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sb.file.compressor.auth.config.RequiredPermission;
import com.sb.file.compressor.core.exception.ApiResponse;
import com.sb.file.compressor.core.exception.ResponseModel;
import com.sb.file.compressor.entity.UserConfiguration;
import com.sb.file.compressor.service.contract.UserConfigurationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.sb.file.compressor.controller.UserControllerApis.USER_CONFIGURATION_ROOT;

/**
 * @author Himal Rai on 2/2/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */
@RestController
@RequestMapping(USER_CONFIGURATION_ROOT)
public class UserConfigurationController {

    private final UserConfigurationService userConfigurationService;
    private ObjectMapper mapper;

    public UserConfigurationController(UserConfigurationService userConfigurationService) {
        this.userConfigurationService = userConfigurationService;
        this.mapper = new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .setSerializationInclusion(JsonInclude.Include.NON_EMPTY).disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @RequiredPermission({"ADMIN", "USER"})
    @PostMapping
    public ResponseEntity<ResponseModel> saveUserConfiguration(@RequestBody UserConfiguration userConfiguration) {
        UserConfiguration configuration = userConfigurationService.save(userConfiguration);
        return ApiResponse.success(configuration);
    }
}
