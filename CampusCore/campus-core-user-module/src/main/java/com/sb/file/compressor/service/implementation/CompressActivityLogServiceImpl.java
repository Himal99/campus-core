package com.sb.file.compressor.service.implementation;

import com.sb.file.compressor.core.enums.CompressStatus;
import com.sb.file.compressor.entity.CompressActivityLog;
import com.sb.file.compressor.entity.User;
import com.sb.file.compressor.repo.CompressActivityLogRepository;
import com.sb.file.compressor.service.contract.CompressActivityLogService;
import com.sb.file.compressor.service.contract.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CompressActivityLogServiceImpl implements CompressActivityLogService {

    private final CompressActivityLogRepository compressActivityLogRepository;
    private final UserService userService;

    public CompressActivityLogServiceImpl(CompressActivityLogRepository compressActivityLogRepository, UserService userService) {
        this.compressActivityLogRepository = compressActivityLogRepository;
        this.userService = userService;
    }

    @Override
    public CompressActivityLog saveCompressLog(CompressActivityLog compressActivityLog) {
        return compressActivityLogRepository.save(compressActivityLog);
    }

    @Override
    public CompressActivityLog saveCompressLog(String email, CompressStatus compressStatus) {
        User currentUser = userService.findByEmail(email);
        CompressActivityLog compressActivityLog = new CompressActivityLog().builder()
                .user(currentUser)
                .status(compressStatus)
                .build();
        return compressActivityLogRepository.save(compressActivityLog);
    }

    @Override
    public Page<CompressActivityLog> getByUserEmail(String userEmail,Pageable pageable) {
        return compressActivityLogRepository.getByUserEmail(userEmail, pageable);
    }

    @Override
    public Page<CompressActivityLog> findAllPageable(Pageable pageable) {
        return compressActivityLogRepository.findAll(pageable);
    }
}
