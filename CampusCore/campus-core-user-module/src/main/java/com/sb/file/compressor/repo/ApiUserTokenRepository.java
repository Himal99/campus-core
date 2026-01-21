package com.sb.file.compressor.repo;

import com.sb.file.compressor.entity.ApiUserToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApiUserTokenRepository extends JpaRepository<ApiUserToken, Long> {

    @Query("SELECT token FROM ApiUserToken token WHERE token.user.email = :email")
    List<ApiUserToken> findAllByUserEmail(@Param("email") String email);
    @Query("SELECT token FROM ApiUserToken token WHERE token.user.email = :email")
    Page<ApiUserToken> findAllByUserEmailPage(@Param("email") String email, Pageable pageable);
    @Query("SELECT token FROM ApiUserToken token WHERE token.value = :val")
    Optional<ApiUserToken> findByTokenValue(@Param("val") String val);
    @Query("SELECT token FROM ApiUserToken token WHERE token.displayName = :displayName AND token.user.id = :userId")
    Optional<ApiUserToken> findByDisplayName(@Param("displayName") String displayName, @Param("userId") Long userId);
}
