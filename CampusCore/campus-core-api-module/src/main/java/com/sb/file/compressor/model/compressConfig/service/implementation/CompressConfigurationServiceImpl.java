package com.sb.file.compressor.model.compressConfig.service.implementation;

import com.sb.file.compressor.model.compressConfig.entity.CompressConfiguration;
import com.sb.file.compressor.model.compressConfig.repo.CompressConfigurationRepository;
import com.sb.file.compressor.model.compressConfig.service.contract.CompressConfigurationService;
import com.sb.file.compressor.core.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Himal Rai on 1/28/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */

@Slf4j
@Service

public class CompressConfigurationServiceImpl implements CompressConfigurationService {

    private final CompressConfigurationRepository configurationRepository;

    public CompressConfigurationServiceImpl(CompressConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
    }

    @Override
    public CompressConfiguration save(CompressConfiguration compressConfiguration) {
        return configurationRepository.save(compressConfiguration);
    }

    @Override
    public CompressConfiguration geById(Long id) {
        return configurationRepository.findById(id).orElseThrow(( ) -> new CustomException("No such data founed"));
    }
}
