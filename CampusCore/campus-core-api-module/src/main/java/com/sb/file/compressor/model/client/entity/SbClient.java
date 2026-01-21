package com.sb.file.compressor.model.client.entity;

import com.sb.file.compressor.model.client.enums.ClientEnum;
import com.sb.file.compressor.core.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author Himal Rai on 1/28/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class SbClient extends BaseEntity<Long> {

    @Enumerated(EnumType.STRING)
    private ClientEnum client;

}
