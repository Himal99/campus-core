package com.sb.file.compressor.model.quickCompressReports.repo;

import com.sb.file.compressor.model.quickCompressReports.entity.QuickCompressionReports;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Himal Rai on 2/25/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */
@Repository
public interface QuickCompressionReportsRepository extends JpaRepository<QuickCompressionReports,Long> {

    Optional<QuickCompressionReports> findBySessionIdAndFileName(String sessionId, String fileName);
}
