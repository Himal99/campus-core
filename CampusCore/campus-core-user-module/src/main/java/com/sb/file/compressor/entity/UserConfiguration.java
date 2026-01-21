package com.sb.file.compressor.entity;

import com.sb.file.compressor.core.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Himal Rai on 2/2/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserConfiguration extends BaseEntity<Long> {
    private boolean image = Boolean.TRUE;
    private boolean pdf = Boolean.TRUE;
    private boolean movie = Boolean.TRUE;


}
