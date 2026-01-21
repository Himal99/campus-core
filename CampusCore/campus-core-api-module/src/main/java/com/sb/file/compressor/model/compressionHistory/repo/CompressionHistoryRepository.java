package com.sb.file.compressor.model.compressionHistory.repo;

import com.sb.file.compressor.model.compressionHistory.entity.CompressionHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Himal Rai on 2/18/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */
@Repository
public interface CompressionHistoryRepository extends JpaRepository<CompressionHistory, Long> {
    Optional<CompressionHistory> findBySessionId(String sessionId);
    List<CompressionHistory> findAllByCompressedByUser(String user);

    @Query(value = "select h from CompressionHistory h where h.compressedByUser =:user and h.sessionId like concat(:sessionId, '%') order by h.createdAt desc ")
    Page<CompressionHistory> findAllByCompressedByUserWithSearchObject(@Param("user") String user,@Param("sessionId") String sessionId, Pageable pageable);

    @Query(value = "select h from CompressionHistory h where h.sessionId like concat(:sessionId, '%') order by h.createdAt desc ")
    Page<CompressionHistory> findAllByCompressedWithSearchObject(@Param("sessionId") String sessionId, Pageable pageable);
}
