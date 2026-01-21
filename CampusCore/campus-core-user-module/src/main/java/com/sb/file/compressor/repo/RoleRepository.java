package com.sb.file.compressor.repo;

import com.sb.file.compressor.entity.Role;
import com.sb.file.compressor.enums.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Himal Rai on 1/14/2024
 * Sb Solutions Nepal pvt.ltd
 * Project sb-back-core.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(Roles name);
}
