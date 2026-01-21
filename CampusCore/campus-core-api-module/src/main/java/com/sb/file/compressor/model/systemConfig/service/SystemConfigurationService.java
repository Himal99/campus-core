package com.sb.file.compressor.model.systemConfig.service;

import com.sb.file.compressor.model.systemConfig.entity.SystemConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Himal Rai on 1/31/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */
public interface SystemConfigurationService {
    SystemConfiguration save(SystemConfiguration systemConfiguration);
    SystemConfiguration findById(Long id);
    List<SystemConfiguration> findAll();
    Page<SystemConfiguration> findAllByPaginationWithSearchObject(String name, Pageable pageable);
    String deleteById(Long id);
}
