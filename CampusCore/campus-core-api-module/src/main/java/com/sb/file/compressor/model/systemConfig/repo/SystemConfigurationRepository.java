package com.sb.file.compressor.model.systemConfig.repo;

import com.sb.file.compressor.model.systemConfig.entity.SystemConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Himal Rai on 1/31/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */
@Repository
public interface SystemConfigurationRepository extends JpaRepository<SystemConfiguration,Long> {

    @Query(value = "select c from SystemConfiguration c where c.serverName like concat(:name,'%')")
    Page<SystemConfiguration> findAllByPaginationWithSearchObject(@Param("name") String name, Pageable pageable);
}
