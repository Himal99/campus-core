package com.sb.file.compressor.model.apiUserLinkMapper.repo;

import com.sb.file.compressor.model.apiUserLinkMapper.entity.ApiUserLinkMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApiUserLinkMapperRepo extends JpaRepository<ApiUserLinkMapper, Long> {
    @Query("SELECT v FROM ApiUserLinkMapper v WHERE v.tokenId = :tokenId AND v.sessionId = :sessionId")
    Optional<ApiUserLinkMapper> findByTokenIdAndSessionId(@Param("tokenId") String id,@Param("sessionId") String sessionId);
}
