package com.sb.file.compressor.service.implementation;

import com.sb.file.compressor.repo.UserConfigurationRepository;
import com.sb.file.compressor.entity.UserConfiguration;
import com.sb.file.compressor.service.contract.UserConfigurationService;
import com.sb.file.compressor.core.exception.CustomException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Himal Rai on 2/2/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */
@Service
public class UserConfigurationImpl implements UserConfigurationService {

    private final UserConfigurationRepository userConfigurationRepository;

    public UserConfigurationImpl(UserConfigurationRepository userConfigurationRepository) {
        this.userConfigurationRepository = userConfigurationRepository;
    }

    @Override
    public UserConfiguration save(UserConfiguration userConfiguration) {
        return userConfigurationRepository.save(userConfiguration);
    }

    @Override
    public UserConfiguration findById(Long id) {

        return userConfigurationRepository.findById(id).orElseThrow(() -> new CustomException("Could not find configuration"));
    }

    @Override
    public List<UserConfiguration> findAll() {
        return userConfigurationRepository.findAll();
    }
}
