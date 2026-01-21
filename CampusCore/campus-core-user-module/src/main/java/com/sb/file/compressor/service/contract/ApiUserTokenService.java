package com.sb.file.compressor.service.contract;

import com.sb.file.compressor.dto.request.ApiUserTokenRequestDto;
import com.sb.file.compressor.dto.response.TokenGenerationLogDto;
import com.sb.file.compressor.entity.ApiUserToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ApiUserTokenService {
    ApiUserToken generateToken(ApiUserToken apiUserToken);
    ApiUserToken generateTokenForApiUser(ApiUserToken apiUserToken);
    ApiUserToken getTokenById(Long id);
    List<TokenGenerationLogDto> getAllToken();
    ApiUserToken updateToken(ApiUserTokenRequestDto apiUserTokenRequestDto);
    String delete(Long id);
    Page<ApiUserToken> findPageable(Pageable pageable);
    List<ApiUserToken> getByUser(String username);
    Page<ApiUserToken> findPageableByEmail(String email, Pageable pageable);
    Optional<ApiUserToken> getByTokenValue(String token);
}
