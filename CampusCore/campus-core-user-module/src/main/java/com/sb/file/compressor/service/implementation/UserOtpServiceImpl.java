package com.sb.file.compressor.service.implementation;

import com.sb.file.compressor.core.exception.CustomException;
import com.sb.file.compressor.entity.UserOtp;
import com.sb.file.compressor.repo.UserOtpRepository;
import com.sb.file.compressor.service.contract.UserOtpService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Himal Rai on 2/22/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */

@Service
public class UserOtpServiceImpl implements UserOtpService {

    private final UserOtpRepository userOtpRepository;

    public UserOtpServiceImpl(UserOtpRepository userOtpRepository) {
        this.userOtpRepository = userOtpRepository;
    }

    @Override
    public List<UserOtp> findAll() {
        return userOtpRepository.findAll();
    }

    @Override
    public UserOtp findOne(Long id) {
        return userOtpRepository.findById(id).orElseThrow(() -> new CustomException("No such user otp found"));
    }

    @Override
    public UserOtp save(UserOtp userOtp) {
        return userOtpRepository.save(userOtp);
    }

    @Override
    public Page<UserOtp> findAllPageable(Object t, Pageable pageable) {
        return null;
    }

    @Override
    public List<UserOtp> saveAll(List<UserOtp> list) {
        return null;
    }

    @Override
    public UserOtp findByEmail(String email) {
        return userOtpRepository.findByEmail(email);
    }

    @Override
    public void delete(UserOtp userOtp) {
        userOtpRepository.delete(userOtp);
    }
}
