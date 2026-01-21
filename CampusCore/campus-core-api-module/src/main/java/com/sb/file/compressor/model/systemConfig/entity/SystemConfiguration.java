package com.sb.file.compressor.model.systemConfig.entity;

import com.sb.file.compressor.core.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Himal Rai on 1/31/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class SystemConfiguration extends BaseEntity<Long> {

    @NotNull(message = "server name cannot be null")
    @NotEmpty(message = "server port cannot be empty")
    private String serverName;

    @NotNull(message = "server password cannot be null")
    @NotEmpty(message = "server password cannot be empty")
    private String serverPassword;

    @NotNull(message = "server username cannot be null")
    @NotEmpty(message = "server username cannot be empty")
    private String serverUserName;

    @NotNull(message = "server root path cannot be null")
    @NotEmpty(message = "server root path cannot be empty")
    private String rootPath;

    private String imageQuality;
    private String pdfQuality;
    private String videoQuality;
    private String serverIp;


}
