package com.sb.file.compressor.service.implementation;

import com.sb.file.compressor.entity.Role;
import com.sb.file.compressor.enums.Roles;
import com.sb.file.compressor.repo.RoleRepository;
import com.sb.file.compressor.service.contract.RoleService;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Himal Rai on 1/14/2024
 * Sb Solutions Nepal pvt.ltd
 * Project sb-back-core.
 */
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;

    public RoleServiceImpl(RoleRepository repository) {
        this.repository = repository;
    }

    @Override
    public Role getRole(Roles name) {
        Optional<Role> role = repository.findByName(name);
        return role.orElse(null);
    }
}
