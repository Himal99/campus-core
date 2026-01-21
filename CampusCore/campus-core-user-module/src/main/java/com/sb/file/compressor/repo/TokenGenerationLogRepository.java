package com.sb.file.compressor.repo;

import com.sb.file.compressor.entity.TokenGenerationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenGenerationLogRepository extends JpaRepository<TokenGenerationLog, Long> {

    @Query("SELECT token FROM TokenGenerationLog token WHERE token.user.email = :email")
    Page<TokenGenerationLog> getByUserEmail(@Param("email")String email, Pageable pageable);





}
