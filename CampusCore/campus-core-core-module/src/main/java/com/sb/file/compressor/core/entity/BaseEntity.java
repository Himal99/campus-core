package com.sb.file.compressor.core.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Himal Rai on 2/1/2024
 * <p>
 * Base entity class, every entity class should extend this
 */

@MappedSuperclass
@NoArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity<PK extends Serializable> extends AbstractPersistable<PK> {

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt = new Date();

    @LastModifiedDate
    @Column(name = "last_modified_at", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastModifiedAt = new Date();

    @CreatedBy
    @Column(name = "created_by_id", updatable = false)
    private Long createdBy;

    @LastModifiedBy
    @Column(name = "modified_by_id")
    private Long modifiedBy;

    @Override
    public void setId(PK id) {
        super.setId(id);
    }
}
