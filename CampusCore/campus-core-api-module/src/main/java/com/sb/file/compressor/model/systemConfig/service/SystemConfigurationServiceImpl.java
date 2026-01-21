package com.sb.file.compressor.model.systemConfig.service;

import com.sb.file.compressor.model.systemConfig.entity.SystemConfiguration;
import com.sb.file.compressor.model.systemConfig.repo.SystemConfigurationRepository;
import com.sb.file.compressor.core.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Himal Rai on 1/31/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */

@Service
@Slf4j
public class SystemConfigurationServiceImpl implements SystemConfigurationService {

    private final SystemConfigurationRepository configurationRepository;

    public SystemConfigurationServiceImpl(SystemConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
    }

    @Override
    public SystemConfiguration save(SystemConfiguration systemConfiguration) {
        return configurationRepository.save(systemConfiguration);
    }

    @Override
    public SystemConfiguration findById(Long id) {
        SystemConfiguration configuration = configurationRepository.findById(id).orElseThrow(() -> new CustomException("Could not find configuration"));
        return configuration;
    }

    @Override
    public List<SystemConfiguration> findAll() {
        return configurationRepository.findAll();
    }

    @Override
    public Page<SystemConfiguration> findAllByPaginationWithSearchObject(String name, Pageable pageable) {
        return configurationRepository.findAllByPaginationWithSearchObject(name == null ? "" : name,pageable);
    }

    @Override
    public String deleteById(Long id) {
       try {
           configurationRepository.deleteById(id);
           return "Success";
       }catch (Exception e) {
           return e.getMessage();
       }
    }
}
