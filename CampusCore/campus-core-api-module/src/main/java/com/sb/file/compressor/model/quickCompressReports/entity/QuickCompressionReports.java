package com.sb.file.compressor.model.quickCompressReports.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sb.file.compressor.core.entity.BaseEntity;

import com.sb.file.compressor.model.compressionHistory.enums.HistoryStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

/**
 * @author Himal Rai on 2/25/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class QuickCompressionReports extends BaseEntity<Long> {
    private String fileName;
    private String originalSize;
    private String compressedSize;

    @Enumerated(EnumType.STRING)
    private HistoryStatus status = HistoryStatus.STARTED;

    private String compressedByUser;

    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startedAt = new Date();

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date completedAt;

    private String fileType;

    private String quality;

    private String sessionId;
    private String filePath;
}
