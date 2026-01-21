package com.sb.file.compressor.model.compressionHistory.service.implementation;

import com.sb.file.compressor.core.exception.CustomException;
import com.sb.file.compressor.model.compressionHistory.entity.CompressionHistory;
import com.sb.file.compressor.model.compressionHistory.repo.CompressionHistoryRepository;
import com.sb.file.compressor.model.compressionHistory.service.contract.CompressionHistoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Himal Rai on 2/18/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */
@Service
public class CompressionHistoryServiceImpl implements CompressionHistoryService {
    private final CompressionHistoryRepository historyRepository;

    public CompressionHistoryServiceImpl(CompressionHistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    @Override
    public List<CompressionHistory> findAll() {
        return null;
    }

    @Override
    public CompressionHistory findOne(Long id) {
        return historyRepository.findById(id).orElseThrow(() -> new CustomException("No such history found"));
    }

    @Override
    public CompressionHistory save(CompressionHistory compressionHistory) {
        return historyRepository.save(compressionHistory);
    }

    @Override
    public Page<CompressionHistory> findAllPageable(Object t, Pageable pageable) {
        return null;
    }

    @Override
    public List<CompressionHistory> saveAll(List<CompressionHistory> list) {
        return historyRepository.saveAll(list);
    }

    @Override
    public CompressionHistory findBySessionId(String sessionId) {
        return this.historyRepository.findBySessionId(sessionId).orElse(new CompressionHistory());
    }

    @Override
    public List<CompressionHistory> findAllByUserName(String userName) {
        return historyRepository.findAllByCompressedByUser(userName);
    }

    @Override
    public Page<CompressionHistory> findAllByCompressedByUserWithSearchObject(String userName, String sessionId, Pageable pageable) {
        return historyRepository.findAllByCompressedByUserWithSearchObject(userName,sessionId == null ? "" : sessionId,pageable);
    }

    @Override
    public Page<CompressionHistory> findAllByCompressedWithSearchObject(String sessionId, Pageable pageable) {
        return historyRepository.findAllByCompressedWithSearchObject(sessionId == null ? "" : sessionId,pageable);
    }
}
