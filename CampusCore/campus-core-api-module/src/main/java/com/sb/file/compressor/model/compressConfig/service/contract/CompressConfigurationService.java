package com.sb.file.compressor.model.compressConfig.service.contract;

import com.sb.file.compressor.model.compressConfig.entity.CompressConfiguration;

/**
 * @author Himal Rai on 1/28/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */
public interface CompressConfigurationService{
    CompressConfiguration save(CompressConfiguration compressConfiguration);
    CompressConfiguration geById(Long id);
}
