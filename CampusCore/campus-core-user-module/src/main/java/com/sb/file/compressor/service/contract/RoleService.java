package com.sb.file.compressor.service.contract;

import com.sb.file.compressor.entity.Role;
import com.sb.file.compressor.enums.Roles;

/**
 * @author Himal Rai on 1/14/2024
 * Sb Solutions Nepal pvt.ltd
 * Project sb-back-core.
 */
public interface RoleService {
    Role getRole(Roles name);
}
