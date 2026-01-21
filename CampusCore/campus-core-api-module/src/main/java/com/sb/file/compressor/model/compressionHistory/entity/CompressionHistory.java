package com.sb.file.compressor.model.compressionHistory.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sb.file.compressor.core.entity.BaseEntity;
import com.sb.file.compressor.model.compressionHistory.enums.HistoryStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

/**
 * @author Himal Rai on 2/18/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompressionHistory extends BaseEntity<Long> {

    @Enumerated(EnumType.STRING)
    private HistoryStatus status = HistoryStatus.STARTED;

    private String compressedByUser;
    private String sessionId;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String historyDetails;

    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startedAt = new Date();

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date completedAt;

    private String fileType;

}
