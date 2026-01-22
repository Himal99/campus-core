package com.sb.file.compressor.model.gurdian.repo;

import com.sb.file.compressor.model.gurdian.entity.Guardian;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * -------------------------------------------------------------
 * |   Author      : Himal Rai
 * |   Department  : JAVA
 * |   Company     : DIGI Hub
 * |   Created     : 1/21/2026 10:08 PM
 * -------------------------------------------------------------
 */
public interface GuardianRepository extends JpaRepository<Guardian, Long> {
    Optional<Guardian> findByEmail(String email);
    Optional<Guardian> findByPhone(String phone);
}
