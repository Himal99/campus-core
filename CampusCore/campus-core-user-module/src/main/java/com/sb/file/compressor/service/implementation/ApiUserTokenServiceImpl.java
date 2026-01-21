package com.sb.file.compressor.service.implementation;

import com.sb.file.compressor.auth.service.ApiUserTokenGenerator;
import com.sb.file.compressor.auth.service.JwtService;
import com.sb.file.compressor.dto.request.ApiUserTokenRequestDto;
import com.sb.file.compressor.dto.response.TokenGenerationLogDto;
import com.sb.file.compressor.entity.ApiUserToken;
import com.sb.file.compressor.entity.TokenGenerationLog;
import com.sb.file.compressor.entity.User;
import com.sb.file.compressor.repo.ApiUserTokenRepository;
import com.sb.file.compressor.service.contract.ApiUserTokenService;
import com.sb.file.compressor.service.contract.TokenGenerationLogService;
import com.sb.file.compressor.service.contract.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ApiUserTokenServiceImpl implements ApiUserTokenService {
    private final ApiUserTokenRepository apiUserTokenRepository;
    private final UserService userService;

    private final TokenGenerationLogService tokenGenerationLogService;

    public ApiUserTokenServiceImpl(ApiUserTokenRepository apiUserTokenRepository, JwtService jwtService, UserService userService, TokenGenerationLogService tokenGenerationLogService) {
        this.apiUserTokenRepository = apiUserTokenRepository;
        this.userService = userService;
        this.tokenGenerationLogService = tokenGenerationLogService;
    }

    @Override
    @Transactional
    public ApiUserToken generateToken(ApiUserToken apiUserToken) {
        User user = userService.findById(apiUserToken.getUserId());
        Optional<ApiUserToken> existingToken = apiUserTokenRepository.findByDisplayName(apiUserToken.getDisplayName(),user.getId());
        existingToken.ifPresent(token -> {
            throw new RuntimeException("Duplicate Name");
        });
        apiUserToken.setUser(user);
        String token = null;
        try {
            token = ApiUserTokenGenerator.encrypt(user.getEmail());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        apiUserToken.setValue(token);
        ApiUserToken generatedToken = apiUserTokenRepository.save(apiUserToken);
        TokenGenerationLog generationLog = new TokenGenerationLog(user, generatedToken.getId());
        tokenGenerationLogService.saveTokenLog(generationLog);
        return generatedToken;
    }

    @Override
    @Transactional
    public ApiUserToken generateTokenForApiUser(ApiUserToken apiUserToken) {
        User loggedInUser = userService.authenticatedUser();
        Optional<ApiUserToken> existingToken = apiUserTokenRepository.findByDisplayName(apiUserToken.getDisplayName(),loggedInUser.getId());
        existingToken.ifPresent(token -> {
                    throw new RuntimeException("Duplicate Name");
                });
        apiUserToken.setUser(loggedInUser);
        String token = null;
        try {
            token = ApiUserTokenGenerator.encrypt(loggedInUser.getEmail());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        apiUserToken.setValue(token);
        ApiUserToken generatedToken = apiUserTokenRepository.save(apiUserToken);
        TokenGenerationLog generationLog = new TokenGenerationLog(loggedInUser, generatedToken.getId());
        tokenGenerationLogService.saveTokenLog(generationLog);
        return generatedToken;
    }

    @Override
    public ApiUserToken getTokenById(Long id) {
     return apiUserTokenRepository.findById(id).orElseThrow(()->new RuntimeException("UserToken Not Found"));
    }

    @Override
    public List<TokenGenerationLogDto> getAllToken() {
        List<ApiUserToken> allUserToken = apiUserTokenRepository.findAll();
        // Group the tokens by username and count the tokens for each user
        Map<String, Long> tokenCountByUser;
        tokenCountByUser = allUserToken
                .stream()
                .collect(Collectors.groupingBy(log -> log.getUser().getUsername(), Collectors.counting()));
        List<TokenGenerationLogDto> tokenList = new ArrayList<>();

        for (Map.Entry<String, Long> entry : tokenCountByUser.entrySet()) {
            Optional<ApiUserToken> tokenOptional = allUserToken.stream()
                    .filter(token -> token.getUser() != null && token.getUser().getEmail().equals(entry.getKey()))
                    .findFirst();
            String userName = tokenOptional.get().getUser().getFullName();
            TokenGenerationLogDto log = new TokenGenerationLogDto(entry.getKey(), entry.getValue(),userName);
            tokenList.add(log);
        }
        return tokenList;
    }

    @Override
    public ApiUserToken updateToken(ApiUserTokenRequestDto apiUserTokenRequestDto) {
        // Fetch the ApiUserToken entity from the database based on the provided id
        ApiUserToken apiUserToken = apiUserTokenRepository.findById(apiUserTokenRequestDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("ApiUserToken not found with id: " + apiUserTokenRequestDto.getId()));

        // Update the attributes of the entity with values from the DTO
        apiUserToken.setDisplayName(apiUserTokenRequestDto.getDisplayName());
//        apiUserToken.setValue(apiUserTokenRequestDto.getApiUserToken());

        // Save the updated entity back to the database
        return apiUserTokenRepository.save(apiUserToken);
    }

    @Override
    public String delete(Long id) {
        if (apiUserTokenRepository.existsById(id)) {
            apiUserTokenRepository.deleteById(id);
            return "User token successfully deleted";
        } else {
            throw new RuntimeException("User token with ID " + id + " not found");
        }
    }


    @Override
    public Page<ApiUserToken> findPageable(Pageable pageable) {
        return apiUserTokenRepository.findAll(pageable);

    }

    @Override
    public List<ApiUserToken> getByUser(String email) {
        return apiUserTokenRepository.findAllByUserEmail(email);
    }

    @Override
    public Page<ApiUserToken> findPageableByEmail(String email, Pageable pageable) {
        return apiUserTokenRepository.findAllByUserEmailPage(email, pageable);
    }

    @Override
    public Optional<ApiUserToken> getByTokenValue(String token) {
        return apiUserTokenRepository.findByTokenValue(token);
    }

}





