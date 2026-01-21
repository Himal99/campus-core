package com.sb.file.compressor.controller;

import com.sb.file.compressor.auth.config.RequiredPermission;
import com.sb.file.compressor.core.exception.ApiResponse;
import com.sb.file.compressor.core.utils.PaginationUtils;
import com.sb.file.compressor.dto.request.ApiUserTokenRequestDto;
import com.sb.file.compressor.dto.response.TokenGenerationLogDto;
import com.sb.file.compressor.entity.ApiUserToken;
import com.sb.file.compressor.entity.User;
import com.sb.file.compressor.service.contract.ApiUserTokenService;
import com.sb.file.compressor.service.contract.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.sb.file.compressor.controller.ApiUserTokenControllerApis.*;

@RestController
@RequiredPermission({"API_USER"})
@RequestMapping(ApiUserTokenControllerApis.ROOT)
public class ApiUserTokenController {
private final ApiUserTokenService apiUserTokenService;
private final UserService userService;

    public ApiUserTokenController(ApiUserTokenService apiUserTokenService, UserService userService) {
        this.apiUserTokenService = apiUserTokenService;
        this.userService = userService;
    }
    @RequiredPermission({"ADMIN"})
    @PostMapping()
    public ResponseEntity<?> tokenGenerator(@RequestBody ApiUserToken apiUserToken) {
        ApiUserToken savedToken =apiUserTokenService.generateToken(apiUserToken);
        return ApiResponse.success(savedToken);
    }

    @RequiredPermission({"ADMIN", "API_USER"})
    @PostMapping("/api-user")
    public ResponseEntity<?> tokenGeneratorForApiUser(@RequestBody ApiUserToken apiUserToken) {
        ApiUserToken savedToken =apiUserTokenService.generateTokenForApiUser(apiUserToken);
        return ApiResponse.success(savedToken);
    }

    @RequiredPermission({"ADMIN", "API_USER"})
    @GetMapping(GET_TOKEN_BY_ID)
    public ResponseEntity<?> getTokenById(@PathVariable("id") Long id) {
        ApiUserToken apiUserToken = apiUserTokenService.getTokenById(id);
        return ApiResponse.success(apiUserToken );
    }

    @RequiredPermission({"ADMIN", "API_USER"})
    @GetMapping(LIST)
    public ResponseEntity<?> getAllTokens() {
        List<TokenGenerationLogDto> apiUserTokens = apiUserTokenService.getAllToken();
        return ApiResponse.success(apiUserTokens);
    }

    @RequiredPermission({"ADMIN", "API_USER"})
    @PutMapping(UPDATE_TOKEN)
    public ResponseEntity<?> updateToken(@RequestBody ApiUserTokenRequestDto apiUserTokenRequestDto) {
        ApiUserToken updatedToken = apiUserTokenService.updateToken(apiUserTokenRequestDto);
        return ApiResponse.success(updatedToken);
    }

    @RequiredPermission({"ADMIN", "API_USER"})
    @GetMapping(PAGEABLE)
    public ResponseEntity<?> findPageable(@RequestParam("size") int size, @RequestParam("page") int page){
        try {
            Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id"));
            Page<ApiUserToken> apiUserTokens = apiUserTokenService.findPageable(pageable);
            return ApiResponse.success(apiUserTokens);
        }catch (Exception e) {
            return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequiredPermission({"ADMIN", "API_USER"})
    @GetMapping(PAGEABLE_BY_EMAIL)
    public ResponseEntity<?> findPageableByEmail(@RequestParam("size") int size, @RequestParam("page") int page, @PathVariable("email") String email){
        try {
            Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id"));
            Page<ApiUserToken> apiUserTokens = apiUserTokenService.findPageableByEmail(email, pageable);
            return ApiResponse.success(apiUserTokens);
        }catch (Exception e) {
            return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequiredPermission({"ADMIN", "API_USER"})
    @DeleteMapping(DELETE_TOKEN)
    public ResponseEntity<?> deleteToken(@PathVariable("id") Long id){
        String userDeleted=apiUserTokenService.delete(id);
        return ApiResponse.success(userDeleted);
    }


}
