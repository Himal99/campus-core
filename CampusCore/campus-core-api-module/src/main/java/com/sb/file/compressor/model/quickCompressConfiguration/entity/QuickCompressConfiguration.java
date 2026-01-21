package com.sb.file.compressor.model.quickCompressConfiguration.entity;

import com.sb.file.compressor.core.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Himal Rai on 2/21/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuickCompressConfiguration extends BaseEntity<Long> {
    private int imageMaxTotalFiles;
    private int imageMaxTotalSize;
    private int pdfMaxTotalFiles;
    private int pdfMaxTotalSize;
    private int videoMaxTotalFiles;
    private int videoMaxTotalSize;
}
