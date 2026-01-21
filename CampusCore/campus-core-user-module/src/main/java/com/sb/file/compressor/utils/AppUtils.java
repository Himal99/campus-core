package com.sb.file.compressor.utils;

import com.sb.file.compressor.entity.Role;
import com.sb.file.compressor.enums.Roles;
import com.sb.file.compressor.service.contract.RoleService;
import com.sb.file.compressor.core.exception.CustomException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Himal Rai on 1/14/2024
 * Sb Solutions Nepal pvt.ltd
 * Project sb-back-core.
 */

@Component
@AllArgsConstructor
public class AppUtils {
    private final RoleService roleService;

    public Set<Role> assignRoleToUser(Set<String> rolesInString) {
        Set<Role> roles = new HashSet<>();
        final Role user = roleService.getRole(Roles.USER);
        if (ObjectUtils.isEmpty(rolesInString)) {
            roles.add(user);
            return roles;
        }

        final Role admin = roleService.getRole(Roles.ADMIN);
        final Role editor = roleService.getRole(Roles.EDITOR);
        final Role apiUser = roleService.getRole(Roles.API_USER);

        for (String role: rolesInString){
            if (role.equalsIgnoreCase(Roles.ADMIN.getValue())) {
                roles.add(admin);
            } else if (role.equalsIgnoreCase(Roles.EDITOR.getValue())) {
                roles.add(editor);
            } else if (role.equalsIgnoreCase(Roles.USER.getValue())) {
                roles.add(user);
            } else if (role.equalsIgnoreCase(Roles.API_USER.getValue())) {
                roles.add(apiUser);
            } else {
                throw new CustomException("Role type invalid!");
            }
        }
        return roles;
    }
}
