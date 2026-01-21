package com.sb.file.compressor.model.apiUserLinkMapper.entity;

import com.sb.file.compressor.core.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiUserLinkMapper extends BaseEntity<Long> {
    private String tokenId;
    private String sessionId;
}
