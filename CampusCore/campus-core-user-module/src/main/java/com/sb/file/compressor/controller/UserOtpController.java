package com.sb.file.compressor.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sb.file.compressor.core.email.MailSenderService;
import com.sb.file.compressor.core.exception.ApiResponse;
import com.sb.file.compressor.core.utils.DateManipulator;
import com.sb.file.compressor.core.utils.StringUtil;
import com.sb.file.compressor.dto.request.UserOtpRequestDto;
import com.sb.file.compressor.dto.request.UserOtpTokenRequestDto;
import com.sb.file.compressor.entity.UserOtp;
import com.sb.file.compressor.service.contract.UserOtpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import static com.sb.file.compressor.controller.UserControllerApis.*;

/**
 * @author Himal Rai on 2/22/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */

@Slf4j
@RestController
@RequestMapping(USER_OTP_BASE)
public class UserOtpController {

    private final UserOtpService otpService;
    private ObjectMapper mapper;
    private final MailSenderService mailSenderService;

    public UserOtpController(UserOtpService otpService, MailSenderService mailSenderService) {
        this.otpService = otpService;
        this.mailSenderService = mailSenderService;

        this.mapper = new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .setSerializationInclusion(JsonInclude.Include.NON_EMPTY).disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
    }

    @PostMapping
    public ResponseEntity<?> generateOTP(@RequestBody UserOtpRequestDto userOtpDto) {
        UserOtp userOtp = otpService.findByEmail(userOtpDto.getEmail());
        if (userOtp == null) {
            userOtp = mapper.convertValue(userOtpDto, UserOtp.class);
        }
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

        return ApiResponse.success(mapper.convertValue(savedUserOtp, UserOtpRequestDto.class));

    }

    @PostMapping(USER_OTP_VERIFY)
    public ResponseEntity<?> verifyOTP(@RequestBody UserOtpTokenRequestDto userOtpDto) {
        UserOtp userOtp = otpService.findByEmail(userOtpDto.getEmail());
        if (userOtp == null) {
            log.error("User Otp not found");
            return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "user OTP not found");
        }

        if (!userOtpDto.getOtp().equalsIgnoreCase(userOtp.getOtp())) {
            log.error("OTP token didn't match");
            return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "OTP token didn't match");
        } else if (userOtp.getExpiry().before(new Date())) {

            log.error("OTP has expired");
            return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "OTP has expired");
        } else {
            otpService.delete(userOtp);
            return ApiResponse.success("Access Granted");
        }

    }

    private void sendOtpMail(UserOtp savedUserOtp) {
        String mailBody = String.format("""
                OTP: %s
                """, savedUserOtp.getOtp());
        mailSenderService.sendSimpleMail(savedUserOtp.getEmail(), "OTP", mailBody);
    }


}
