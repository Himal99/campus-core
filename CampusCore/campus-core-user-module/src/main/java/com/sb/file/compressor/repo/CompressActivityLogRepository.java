package com.sb.file.compressor.repo;

import com.sb.file.compressor.entity.CompressActivityLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompressActivityLogRepository extends JpaRepository<CompressActivityLog, Long> {

    @Query("SELECT log FROM CompressActivityLog log WHERE log.user.email = :email")
    Page<CompressActivityLog> getByUserEmail(@Param("email") String email, Pageable pageable);
}
