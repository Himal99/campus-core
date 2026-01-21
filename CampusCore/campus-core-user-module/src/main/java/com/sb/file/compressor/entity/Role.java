package com.sb.file.compressor.entity;

import com.sb.file.compressor.core.entity.BaseEntity;
import com.sb.file.compressor.enums.Roles;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Himal Rai on 1/14/2024
 * Sb Solutions Nepal pvt.ltd
 * Project sb-back-core.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Role extends BaseEntity<Long> {

    @Enumerated(EnumType.STRING)
    private Roles name;


}
