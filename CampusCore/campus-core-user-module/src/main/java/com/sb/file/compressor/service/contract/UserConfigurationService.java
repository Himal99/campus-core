package com.sb.file.compressor.service.contract;

import com.sb.file.compressor.entity.UserConfiguration;

import java.util.List;

/**
 * @author Himal Rai on 2/2/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */
public interface UserConfigurationService {
    UserConfiguration save(UserConfiguration userConfiguration);
    UserConfiguration findById(Long id);
    List<UserConfiguration> findAll();
}
