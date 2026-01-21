package com.sb.file.compressor.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sb.file.compressor.core.entity.BaseEntity;
import com.sb.file.compressor.core.enums.CompressStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Table(name="Compress_Activity_log")
public class CompressActivityLog extends BaseEntity<Long> {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdDate = new Date();
    @ManyToOne
    private User user;
    @Enumerated(EnumType.STRING)
    private CompressStatus status = CompressStatus.FAIL;
}
