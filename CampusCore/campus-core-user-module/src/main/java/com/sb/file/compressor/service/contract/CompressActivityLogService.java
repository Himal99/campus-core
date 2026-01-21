package com.sb.file.compressor.service.contract;

import com.sb.file.compressor.core.enums.CompressStatus;
import com.sb.file.compressor.entity.CompressActivityLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CompressActivityLogService {
    CompressActivityLog saveCompressLog(CompressActivityLog compressActivityLog);
    CompressActivityLog saveCompressLog(String email, CompressStatus compressStatus);
    Page<CompressActivityLog> getByUserEmail(String userEmail, Pageable pageable);
    Page<CompressActivityLog> findAllPageable(Pageable pageable);

}
