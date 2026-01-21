package com.sb.file.compressor.model.compressionHistory.service.contract;

import com.sb.file.compressor.core.service.BaseService;
import com.sb.file.compressor.model.compressionHistory.entity.CompressionHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Himal Rai on 2/18/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */
public interface CompressionHistoryService extends BaseService<CompressionHistory> {
    CompressionHistory findBySessionId(String sessionId);
    List<CompressionHistory> findAllByUserName(String userName);
    Page<CompressionHistory> findAllByCompressedByUserWithSearchObject(String userName, String sessionId, Pageable pageable);

    Page<CompressionHistory> findAllByCompressedWithSearchObject(String sessionId, Pageable pageable);
}
