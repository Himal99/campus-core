package com.sb.file.compressor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sb.file.compressor.core.email.MailSenderService;
import com.sb.file.compressor.core.exception.ApiResponse;
import com.sb.file.compressor.core.utils.DateManipulator;
import com.sb.file.compressor.core.utils.StringUtil;
import com.sb.file.compressor.dto.request.ForgotPasswordRequestDto;
import com.sb.file.compressor.dto.request.UserOtpTokenRequestDto;
import com.sb.file.compressor.entity.User;
import com.sb.file.compressor.entity.UserOtp;
import com.sb.file.compressor.service.contract.UserOtpService;
import com.sb.file.compressor.service.contract.UserService;
import com.sb.file.compressor.utils.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;

/**
 * @author Himal Rai on 3/3/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */
@RestController
@RequestMapping(AppConstants.API+"/reset-password")
@Slf4j
public class ForgotPasswordController {

    private final UserService userService;
    private final UserOtpService otpService;
    private final ObjectMapper mapper;
    private final MailSenderService mailSenderService;
    private final BCryptPasswordEncoder passwordEncoder;


    public ForgotPasswordController(UserService userService, UserOtpService otpService, ObjectMapper mapper, MailSenderService mailSenderService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.otpService = otpService;
        this.mapper = mapper;
        this.mailSenderService = mailSenderService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/{email}")
    public ResponseEntity<?> confirmUser(@PathVariable("email") String email){
        User user = userService.findByEmail(email);

        UserOtp newUserOtp = new UserOtp();
        UserOtp userOtp = otpService.findByEmail(user.getEmail());
        if (userOtp == null) {
            newUserOtp.setUserName(user.getFirstName() +" " + user.getLastName());
            newUserOtp.setEmail(user.getEmail());
            newUserOtp.setMobile(user.getPhoneNumber());
            newUserOtp.setOtp(StringUtil.generateNumber(4));

            DateManipulator dateManipulator = new DateManipulator(new Date());
            newUserOtp.setExpiry(dateManipulator.addMinutes(30));

            UserOtp savedUserOtp = otpService.save(newUserOtp);


            try {
                sendOtpMail(savedUserOtp);
            } catch (Exception e) {
                e.printStackTrace();
                return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "Error sending OTP mail");
            }
        } else {
            userOtp.setOtp(StringUtil.generateNumber(4));

            DateManipulator dateManipulator = new DateManipulator(new Date());
            userOtp.setExpiry(dateManipulator.addMinutes(30));

            UserOtp savedUserOtp = otpService.save(userOtp);

            try {
                sendOtpMail(savedUserOtp);
            } catch (Exception e) {
                e.printStackTrace();
                return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "Error sending OTP mail");
            }
        }

        return ApiResponse.success("success");
    }


    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOTP(@RequestBody UserOtpTokenRequestDto userOtpDto) {
        UserOtp userOtp = otpService.findByEmail(userOtpDto.getEmail());
        if (userOtp == null) {
            log.error("User Otp not found");
            return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid email");
        }

        if (!userOtpDto.getOtp().equalsIgnoreCase(userOtp.getOtp())) {
            log.error("OTP token didn't match");
            return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "OTP token didn't match");
        } else if (userOtp.getExpiry().before(new Date())) {

            log.error("OTP has expired");
            return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "OTP has expired");
        } else {
            return ApiResponse.success(HttpStatus.OK,"Access Granted",new HashMap(){{
                put("email",userOtp.getEmail());
                put("otp",userOtp.getOtp());
            }});
        }

    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestBody ForgotPasswordRequestDto forgotPasswordRequestDto){

        User user = userService.findByEmail(forgotPasswordRequestDto.getEmail());
        UserOtp userOtp = otpService.findByEmail(user.getEmail());
        if (userOtp == null) {
            log.error("User Otp not found");
            return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "No otp found or OTP has expired");
        }

        if (user == null) {
            log.error("User not found");
            return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid email");
        }

        user.setPassword(passwordEncoder.encode(forgotPasswordRequestDto.getNewPassword()));
        userService.save(user);

        return ApiResponse.success("Password reset successfully");
    }


    private void sendOtpMail(UserOtp savedUserOtp) {
        String mailBody = String.format("""
                OTP: %s
                """, savedUserOtp.getOtp());
        mailSenderService.sendSimpleMail(savedUserOtp.getEmail(), "OTP", mailBody);
    }

}
