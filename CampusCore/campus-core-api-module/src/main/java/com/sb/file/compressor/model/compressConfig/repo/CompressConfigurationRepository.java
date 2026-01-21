package com.sb.file.compressor.model.compressConfig.repo;

import com.sb.file.compressor.model.compressConfig.entity.CompressConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Himal Rai on 1/28/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */

@Repository
public interface CompressConfigurationRepository extends JpaRepository<CompressConfiguration,Long> {

}
