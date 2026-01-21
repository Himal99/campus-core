package com.sb.file.compressor.entity;

import com.sb.file.compressor.core.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name="api_user_token")
public class ApiUserToken extends BaseEntity<Long> {
    @ManyToOne
    private User user;

    @Column(unique = true)
    private String displayName;

    @Column(columnDefinition = "VARCHAR(MAX)")
    private String value;

    private String ipAddress;

    @Transient
    private Long userId;
}
