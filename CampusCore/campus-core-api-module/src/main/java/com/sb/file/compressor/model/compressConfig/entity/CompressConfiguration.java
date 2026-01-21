package com.sb.file.compressor.model.compressConfig.entity;

import com.sb.file.compressor.core.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Himal Rai on 1/28/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CompressConfiguration extends BaseEntity<Long> {

    private float pdfQualityLevel,pngQualityLevel,jpegQualityLevel,videoQualityLevel,audioQualityLevel;

    private int pdfMaxSize,pngMaxSize,jpegMaxSize;

    private int videoMaxSize,audioMaxSize;
}
