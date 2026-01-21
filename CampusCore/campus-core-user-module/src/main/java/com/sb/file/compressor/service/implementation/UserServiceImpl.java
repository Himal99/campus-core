package com.sb.file.compressor.service.implementation;

import com.sb.file.compressor.core.email.MailSenderService;
import com.sb.file.compressor.core.enums.Status;
import com.sb.file.compressor.core.exception.CustomException;
import com.sb.file.compressor.core.uploads.FileUploadUtils;
import com.sb.file.compressor.dto.mapper.UserMapper;
import com.sb.file.compressor.dto.request.ChangePasswordRequestDto;
import com.sb.file.compressor.dto.request.UserRegisterRequest;
import com.sb.file.compressor.dto.request.UserUpdateRequestDto;
import com.sb.file.compressor.dto.response.ConfirmPasswordResponseDto;
import com.sb.file.compressor.entity.User;
import com.sb.file.compressor.repo.UserRepository;
import com.sb.file.compressor.service.contract.UserService;
import com.sb.file.compressor.utils.AppUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Himal Rai on 1/14/2024
 * Sb Solutions Nepal pvt.ltd
 * Project sb-back-core.
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final AppUtils appUtils;
    private final UserMapper userMapper;
    private final MailSenderService mailSenderService;

    @Value("${system.login.url}")
    private String loginUrl;

    public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder, AppUtils appUtils, UserMapper userMapper, MailSenderService mailSenderService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.appUtils = appUtils;
        this.userMapper = userMapper;
        this.mailSenderService = mailSenderService;
    }



    @Override
    public User authenticatedUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            return (User) authentication.getPrincipal();
        } else {
            throw new RuntimeException("User is not authenticated");
        }
    }

    @Override
    public String logout() {
        User user = this.authenticatedUser();
        log.info("Logging out user: {}", user.getUsername());
        SecurityContextHolder.getContext().setAuthentication(null);
        return "Logged out successfully";
    }


    @Override
    public Long addOrEditUser(UserRegisterRequest userRegisterRequest) {

        User userTemp = new User();
        if (userRegisterRequest.getId() != null){
            userTemp = this.repository.findById(userRegisterRequest.getId()).orElse(new User());
        }

        this.validateUserAddOrEdit(userRegisterRequest, userTemp);

        User user = this.userMapper.convertToUser(userRegisterRequest, userTemp);

        User userSaved = this.repository.save(user);

        try {
            if (userRegisterRequest.getId() == null) {
                this.sendAccountCreatedMailNotification(userRegisterRequest);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return userSaved.getId();

        }
        return userSaved.getId();

    }

    @Override
    public List<User> getUsers() {
        return this.repository.findAll();
    }

    @Override
    public User changePassword(ChangePasswordRequestDto requestDto) {
        User user = repository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new CustomException("User not found"));

        if (passwordEncoder.matches(requestDto.getOldPassword(), user.getEncodedPassword())) {
            user.setPassword(passwordEncoder.encode(requestDto.getNewPassword()));
            user.setHasChangedPassword(Boolean.TRUE);
            user.setHasResetPassword(Boolean.FALSE);
        } else {
            throw new CustomException("Old password is incorrect.");
        }

        return repository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return repository.findByEmail(email).orElseThrow(() -> new CustomException("User not found"));
    }

    @Override
    public String userProfile(byte[] fileInByte, String folderName, String documentName, String email) {
        String path = null;

        User user = findByEmail(email);

        try {
            path = FileUploadUtils.uploadFile(fileInByte, folderName, documentName);

            user.setUserProfile(path);
            repository.save(user);

        } catch (Exception e) {
            e.printStackTrace();

            throw new CustomException("can't upload file");
        }
        return path;
    }

    @Override
    public ConfirmPasswordResponseDto confirmPassword(String email, String password) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User not found"));

        return passwordEncoder.matches(password, user.getEncodedPassword()) ? new ConfirmPasswordResponseDto(true) :
                new ConfirmPasswordResponseDto(false);

    }

    @Override
    public Page<User> findPageable(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<User> findPageableWithSearchObject(String searchObject, Pageable pageable) {

        return repository.userFilterByName(searchObject == null ? "" : searchObject, pageable);
    }

    @Override
    public User changeUserStatus(String email,boolean status) {
        User user = findByEmail(email);
        user.setStatus(status ? Status.ACTIVE : Status.INACTIVE);
        return repository.save(user);
    }

    @Override
    public String currentUserEmail() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.isAuthenticated()){
            User userDetails = (User) authentication.getPrincipal();
            return userDetails.getEmail();
        } else {
            throw new RuntimeException("User is not authenticated");
        }
    }

    @Override
    public User updateUser(UserUpdateRequestDto userUpdateRequestDto) {
        User user = repository.findById(userUpdateRequestDto.getId()).orElseThrow(() -> new CustomException("User is not found"));
        user.setFirstName(userUpdateRequestDto.getFirstName());
        user.setLastName(userUpdateRequestDto.getLastName());
        if (userUpdateRequestDto.getEmail().equalsIgnoreCase(user.getEmail())) {
            user.setEmail(userUpdateRequestDto.getEmail());
        } else {
            if (!userUpdateRequestDto.getEmail().equalsIgnoreCase(user.getEmail())) {
                User existingByEmail = findByEmail(userUpdateRequestDto.getEmail());
                if (existingByEmail != null && existingByEmail.getId() != userUpdateRequestDto.getId()) {
                    throw new CustomException("Email already exists.");
                }
            } else {
                user.setEmail(userUpdateRequestDto.getEmail());
            }
        }

        user.setPhoneNumber(userUpdateRequestDto.getPhoneNumber());
        user.setServerCompressor(userUpdateRequestDto.getServerCompressor());
        return repository.save(user);
    }

    @Override
    public User findById(Long id) {
        return repository.findById(id).orElseThrow(()-> new CustomException("Could not find user"));
    }

    @Override
    public User save(User user) {
        return repository.save(user);
    }

    private void validateUserAddOrEdit(UserRegisterRequest userRegisterRequest, User userTemp) {
        this.validateEmail(userRegisterRequest, userTemp);

    }

    private void validateEmail(UserRegisterRequest userRegisterRequest, User userTemp) {
        if (userTemp.getId() == null) {
            // add
            boolean existsByEmail = this.repository.existsByEmail(userRegisterRequest.getEmail());
            if (existsByEmail) {
                throw new CustomException("User already exists.");
            }
        } else {
            boolean existsByEmail = this.repository.existsByEmailAndIdNot(userRegisterRequest.getEmail(), userTemp.getId());
            if (existsByEmail) {
                throw new CustomException("User already exists.");
            }
        }
    }

    private void sendAccountCreatedMailNotification(UserRegisterRequest registerRequest) {
        String mailBody = String.format("""
                        Hi, %s
                        your account is created for file compressor system, please do log in using following credentials.
                        UserEmail: %s,
                        Password: %s,
                        Login url: %s""", registerRequest.getFirstName(),
                registerRequest.getEmail(), registerRequest.getPassword(),loginUrl);

        this.mailSenderService.sendSimpleMail(registerRequest.getEmail(),"Account Created",mailBody);
    }
}
