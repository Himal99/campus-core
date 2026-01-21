package com.sb.file.compressor.model.quickCompressConfiguration.repo;

import com.sb.file.compressor.model.quickCompressConfiguration.entity.QuickCompressConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Himal Rai on 2/21/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */
@Repository
public interface QuickCompressConfigurationRepo extends JpaRepository<QuickCompressConfiguration,Long> {
}
