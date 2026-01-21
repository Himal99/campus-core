package com.sb.file.compressor.model.quickCompressConfiguration.service.impl;

import com.sb.file.compressor.core.exception.CustomException;
import com.sb.file.compressor.model.quickCompressConfiguration.entity.QuickCompressConfiguration;
import com.sb.file.compressor.model.quickCompressConfiguration.repo.QuickCompressConfigurationRepo;
import com.sb.file.compressor.model.quickCompressConfiguration.service.contract.QuickCompressConfigurationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Himal Rai on 2/21/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */

@Service
public class QuickCompressConfigurationServiceImpl implements QuickCompressConfigurationService {

    private final QuickCompressConfigurationRepo quickCompressConfigurationRepo;

    public QuickCompressConfigurationServiceImpl(QuickCompressConfigurationRepo compressConfigurationRepo) {
        this.quickCompressConfigurationRepo = compressConfigurationRepo;
    }

    @Override
    public List<QuickCompressConfiguration> findAll() {
        return quickCompressConfigurationRepo.findAll();
    }

    @Override
    public QuickCompressConfiguration findOne(Long id) {
        return quickCompressConfigurationRepo.findById(id).orElseThrow(() -> new CustomException("No such QuickCompressConfiguration found for id " + id));
    }

    @Override
    public QuickCompressConfiguration save(QuickCompressConfiguration quickCompressConfiguration) {
        return quickCompressConfigurationRepo.save(quickCompressConfiguration);
    }

    @Override
    public Page<QuickCompressConfiguration> findAllPageable(Object t, Pageable pageable) {
        return null;
    }

    @Override
    public List<QuickCompressConfiguration> saveAll(List<QuickCompressConfiguration> list) {
        return null;
    }
}
