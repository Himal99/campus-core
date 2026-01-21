package com.sb.file.compressor.model.quickCompressReports.service.contract;

import com.sb.file.compressor.core.service.BaseService;
import com.sb.file.compressor.model.quickCompressReports.entity.QuickCompressionReports;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.swing.text.html.Option;
import java.util.Optional;

/**
 * @author Himal Rai on 2/25/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */
public interface QuickCompressionReportsService extends BaseService<QuickCompressionReports> {
    Optional<QuickCompressionReports> findBySessionIdAndFilename(String sessionId, String filename);

    Page<QuickCompressionReports> findAllPage(Pageable pageable);
}
