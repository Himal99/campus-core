package com.sb.file.compressor.repo;

import com.sb.file.compressor.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * @author Himal Rai on 1/14/2024
 * Sb Solutions Nepal pvt.ltd
 * Project sb-back-core.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "select u from User u where u.firstName like concat(:name,'%')")
    Page<User> userFilterByName(@Param("name") String name, Pageable pageable);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, long userId);
}
