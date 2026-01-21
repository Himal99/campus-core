package com.sb.file.compressor.service.implementation;

import com.sb.file.compressor.dto.response.TokenGenerationLogDto;
import com.sb.file.compressor.entity.TokenGenerationLog;
import com.sb.file.compressor.repo.TokenGenerationLogRepository;
import com.sb.file.compressor.service.contract.TokenGenerationLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TokenGenerationLogServiceImpl implements TokenGenerationLogService {
    private final TokenGenerationLogRepository tokenGenerationLogRepository;

    public TokenGenerationLogServiceImpl(TokenGenerationLogRepository tokenGenerationLogRepository) {
        this.tokenGenerationLogRepository = tokenGenerationLogRepository;
    }

    @Override
    public TokenGenerationLog saveTokenLog(TokenGenerationLog tokenGenerationLog) {
        return tokenGenerationLogRepository.save(tokenGenerationLog) ;
    }

    @Override
    public Page<TokenGenerationLog> getByUserEmail(String email, Pageable pageable) {
        return tokenGenerationLogRepository.getByUserEmail(email, pageable);
    }

    @Override
    public List<TokenGenerationLogDto> findAll(){
    List<TokenGenerationLog> allUserToken = tokenGenerationLogRepository.findAll();
        // Group the tokens by username and count the tokens for each user
        Map<String, Long> tokenCountByUser;
        tokenCountByUser = allUserToken
                .stream()
                .collect(Collectors.groupingBy(log -> log.getUser().getUsername(), Collectors.counting()));
        List<TokenGenerationLogDto> tokenGenerationLogs = new ArrayList<>();

//        for (Map.Entry<String, Long> entry : tokenCountByUser.entrySet()) {
//            TokenGenerationLogDto log = new TokenGenerationLogDto(entry.getKey(), entry.getValue(), );
//            tokenGenerationLogs.add(log);
//        }
       return tokenGenerationLogs;
    }

}
