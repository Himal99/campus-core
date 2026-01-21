package com.sb.file.compressor.service.contract;

import com.sb.file.compressor.dto.response.TokenGenerationLogDto;
import com.sb.file.compressor.entity.TokenGenerationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface  TokenGenerationLogService {

    TokenGenerationLog saveTokenLog(TokenGenerationLog tokenGenerationLog);
    Page<TokenGenerationLog> getByUserEmail(String userEmail, Pageable pageable);
    List<TokenGenerationLogDto> findAll();

}
