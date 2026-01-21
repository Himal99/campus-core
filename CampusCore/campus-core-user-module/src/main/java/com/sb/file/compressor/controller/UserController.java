package com.sb.file.compressor.controller;

import com.sb.file.compressor.auth.config.RequiredPermission;
import com.sb.file.compressor.auth.provider.AppAuthenticationProvider;
import com.sb.file.compressor.auth.service.JwtService;
import com.sb.file.compressor.core.email.MailSenderService;
import com.sb.file.compressor.core.exception.ApiResponse;
import com.sb.file.compressor.core.exception.CustomException;
import com.sb.file.compressor.core.exception.ResponseModel;
import com.sb.file.compressor.dto.request.*;
import com.sb.file.compressor.dto.response.ConfirmPasswordResponseDto;
import com.sb.file.compressor.dto.response.LoginResponse;
import com.sb.file.compressor.entity.User;
import com.sb.file.compressor.service.contract.UserService;
import com.sb.file.compressor.utils.AppConstants;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.sb.file.compressor.controller.UserControllerApis.*;

/**
 * @author Himal Rai on 1/14/2024
 * Sb Solutions Nepal pvt.ltd
 * Project sb-back-core.
 */

@RestController
@RequestMapping(AppConstants.AUTH)
@AllArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    private final AppAuthenticationProvider appAuthenticationProvider;
    private final BCryptPasswordEncoder passwordEncoder;

    private final JwtService jwtService;
    private final MailSenderService mailSenderService;

    @PostMapping(REGISTER)
    ResponseEntity<ResponseModel> register(@Validated @RequestBody UserRegisterRequest userRegisterRequest) {
        Long userId = null;
        try {
            userId = this.userService.addOrEditUser(userRegisterRequest);
        } catch (CustomException e) {
            return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return ApiResponse.success(HttpStatus.CREATED, "Success", userId);
    }


    @PostMapping(LOGIN)
    ResponseEntity<ResponseModel> login(@Valid @RequestBody LoginRequest req) {
        Authentication authentication = null;
        try {
            authentication = appAuthenticationProvider
                    .authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
        } catch (Exception e) {
            return ApiResponse.unAuthorizedError(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtService.generateToken(req.getEmail());

        User user = userService.findByEmail(req.getEmail());
        return ApiResponse.success(HttpStatus.OK, "Login is successful",
                new LoginResponse(jwtToken, "Token generated successfully!", user.getFullName(), user.getUserRole(),
                        user.getEmail(), user.getServerCompressor(), user.getUserConfiguration().isImage(), user.getUserConfiguration().isPdf(),
                        user.getUserConfiguration().isMovie(), user.getHasChangedPassword() == null ? Boolean.TRUE : user.getHasChangedPassword()));
    }

    @PostMapping(LOGIN_API_USERS)
    ResponseEntity<ResponseModel> loginApiUsers(@Valid @RequestBody LoginRequest req) {
        Authentication authentication = null;
        try {
            authentication = appAuthenticationProvider
                    .authenticateApiUsers(new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
        } catch (Exception e) {
            return ApiResponse.unAuthorizedError(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtService.generateTokenForApiUser(req.getEmail());

        User user = userService.findByEmail(req.getEmail());

        return ApiResponse.success(HttpStatus.OK, "Login is successful",
                    new LoginResponse(jwtToken, "Token generated successfully!",
                        user.getEmail()));
    }

    @GetMapping(REFRESH_TOKEN)
    ResponseEntity<ResponseModel> refreshToken(@RequestParam("email") String email) {

        return ApiResponse.success(HttpStatus.OK, "Success",
                new LoginResponse(jwtService.generateToken(email), "Token generated successfully!"));
    }


    @PostMapping(UPLOAD_PROFILE)
    public ResponseEntity<?> uploadProfile(@RequestBody MultipartFile file, @RequestParam("email") String email) {

        try {
            String path = this.userService.userProfile(file.getBytes(), "user-profile", file.getOriginalFilename(), email);
            return ApiResponse.success(path);
        } catch (Exception e) {
            return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    @PostMapping(CHANGE_PASSWORD)
    ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequestDto req) {
        try {
            User user = this.userService.changePassword(req);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    @PostMapping(RESET_PASSWORD)
    ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequestDto req) {
        try {
            User user = this.userService.findByEmail(req.getEmail());
            user.setPassword(passwordEncoder.encode(req.getPassword()));
            user.setHasChangedPassword(Boolean.FALSE);
            user.setHasResetPassword(Boolean.TRUE);
            User updatedUser = userService.save(user);
            String emailBody = String.format("""
                    Dear, %s
                    Your password has been changed.
                    Please re log in using following password:
                    New password: %s
                    """, updatedUser.getFirstName(), req.getPassword());

            try {
                mailSenderService.sendSimpleMail(req.getEmail(), "Password changed - File Compressor", emailBody);
            } catch (Exception e) {
                e.printStackTrace();
                return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot send email to " + req.getEmail());
            }
            return ApiResponse.success("Password reset successfully");
        } catch (Exception e) {
            return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    @PostMapping(CONFIRM_PASSWORD)
    public ResponseEntity<?> confirmPassword(@Valid @RequestBody ConfirmPasswordRequestDto requestDto) {
        try {
            ConfirmPasswordResponseDto response = this.userService.confirmPassword(requestDto.getEmail(), requestDto.getPassword());
            return ApiResponse.success(response);
        } catch (Exception e) {
            return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(CHANGE_STATUS)
    @RequiredPermission({"ADMIN"})
    ResponseEntity<ResponseModel> changeUserStatus(@PathVariable("email") String email,
                                                   @PathVariable("status") boolean status) {
        return ApiResponse.success(userService.changeUserStatus(email, status));
    }




}
