package com.sb.file.compressor.repo;


import com.sb.file.compressor.entity.UserOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Himal Rai on 2/22/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */

@Repository
public interface UserOtpRepository extends JpaRepository<UserOtp,Long> {
    UserOtp findByEmail(String email);
}
