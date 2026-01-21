package com.sb.file.compressor.model.permission;


import com.sb.file.compressor.core.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



/**
 * Created by Rujan Maharjan on 3/25/2019.
 */

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Permission extends BaseEntity<Long> {

    @Column(unique = true, nullable = false)
    private String permissionName;
    private String frontUrl;
    private String faIcon;


}

