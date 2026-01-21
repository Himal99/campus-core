package com.sb.file.compressor.service.contract;


import com.sb.file.compressor.core.service.BaseService;
import com.sb.file.compressor.entity.UserOtp;

/**
 * @author Himal Rai on 2/22/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */
public interface UserOtpService extends BaseService<UserOtp> {
    UserOtp findByEmail(String email);
    void delete(UserOtp userOtp);
}
