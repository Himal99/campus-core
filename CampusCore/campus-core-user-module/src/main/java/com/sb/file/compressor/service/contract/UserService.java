package com.sb.file.compressor.service.contract;

import com.sb.file.compressor.dto.request.UserUpdateRequestDto;
import com.sb.file.compressor.dto.response.ConfirmPasswordResponseDto;
import com.sb.file.compressor.entity.User;
import com.sb.file.compressor.dto.request.ChangePasswordRequestDto;
import com.sb.file.compressor.dto.request.UserRegisterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Himal Rai on 1/14/2024
 * Sb Solutions Nepal pvt.ltd
 * Project sb-back-core.
 */
public interface UserService {


    User authenticatedUser();

    String logout();

    Long addOrEditUser(UserRegisterRequest userRegisterRequest);

    List<User> getUsers();

    User changePassword(ChangePasswordRequestDto requestDto);

    User findByEmail(String email);

    String userProfile(byte[] fileInByte, String folderName,
                       String documentName, String email);

    ConfirmPasswordResponseDto confirmPassword(String email, String password);

    Page<User> findPageable(Pageable pageable);
    Page<User> findPageableWithSearchObject(String searchObject, Pageable pageable);

    User changeUserStatus(String email, boolean status);

    String currentUserEmail();

    User updateUser(UserUpdateRequestDto userUpdateRequestDto);

    User findById(Long id);

    User save(User user);
}
