package com.sb.file.compressor.model.quickCompressReports.service.impl;

import com.sb.file.compressor.core.exception.CustomException;
import com.sb.file.compressor.model.quickCompressReports.entity.QuickCompressionReports;
import com.sb.file.compressor.model.quickCompressReports.repo.QuickCompressionReportsRepository;
import com.sb.file.compressor.model.quickCompressReports.service.contract.QuickCompressionReportsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Himal Rai on 2/25/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */
@Service
public class QuickCompressionReportsServiceImpl implements QuickCompressionReportsService {

    private final QuickCompressionReportsRepository quickCompressionReportsRepository;

    public QuickCompressionReportsServiceImpl(QuickCompressionReportsRepository quickCompressionReportsRepository) {
        this.quickCompressionReportsRepository = quickCompressionReportsRepository;
    }

    @Override
    public List<QuickCompressionReports> findAll() {
        return quickCompressionReportsRepository.findAll();
    }

    @Override
    public QuickCompressionReports findOne(Long id) {
        return quickCompressionReportsRepository.findById(id).orElseThrow(() -> new CustomException("Could not find such quick-compression report"));
    }

    @Override
    public QuickCompressionReports save(QuickCompressionReports quickCompressionReports) {
        return quickCompressionReportsRepository.save(quickCompressionReports);
    }

    @Override
    public Page<QuickCompressionReports> findAllPageable(Object t, Pageable pageable) {
        return null;
    }

    @Override
    public List<QuickCompressionReports> saveAll(List<QuickCompressionReports> list) {
        return quickCompressionReportsRepository.saveAll(list);
    }

    @Override
    public Optional<QuickCompressionReports> findBySessionIdAndFilename(String sessionId, String filename) {
        return quickCompressionReportsRepository.findBySessionIdAndFileName(sessionId, filename);
    }

    @Override
    public Page<QuickCompressionReports> findAllPage(Pageable pageable) {
        return quickCompressionReportsRepository.findAll(pageable);
    }
}
