package com.sb.file.compressor.entity;

import com.sb.file.compressor.core.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Himal Rai on 2/22/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserOtp extends BaseEntity<Long> {
    private String userName;

    private String email;

    private String mobile;

    private String otp;

    @Temporal(TemporalType.TIMESTAMP)
    private Date expiry;
}
