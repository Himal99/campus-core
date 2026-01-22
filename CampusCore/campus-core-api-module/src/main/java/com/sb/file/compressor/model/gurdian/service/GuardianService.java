package com.sb.file.compressor.model.gurdian.service;

import com.sb.file.compressor.core.service.BaseService;
import com.sb.file.compressor.model.gurdian.entity.Guardian;
import com.sb.file.compressor.model.gurdian.repo.GuardianRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * -------------------------------------------------------------
 * |   Author      : Himal Rai
 * |   Department  : JAVA
 * |   Company     : DIGI Hub
 * |   Created     : 1/21/2026 10:09 PM
 * -------------------------------------------------------------
 */

@Service
public class GuardianService implements BaseService<Guardian> {

    private final GuardianRepository guardianRepository;

    public GuardianService(GuardianRepository guardianRepository) {
        this.guardianRepository = guardianRepository;
    }

    @Override
    public List<Guardian> findAll() {
        return guardianRepository.findAll();
    }

    @Override
    public Guardian findOne(Long id) {
        return guardianRepository.findById(id).orElse(null);
    }

    @Override
    public Guardian save(Guardian guardian) {
        return guardianRepository.save(guardian);
    }

    @Override
    public Page<Guardian> findAllPageable(Object t, Pageable pageable) {
        return guardianRepository.findAll(pageable);
    }

    @Override
    public List<Guardian> saveAll(List<Guardian> list) {
        return guardianRepository.saveAll(list);
    }
}
