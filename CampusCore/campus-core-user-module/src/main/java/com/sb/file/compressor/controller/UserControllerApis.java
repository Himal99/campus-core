package com.sb.file.compressor.controller;

import com.sb.file.compressor.core.contants.BaseControllerApis;
import lombok.Data;

/**
 * @author Himal Rai on 1/30/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */

@Data
public class UserControllerApis extends BaseControllerApis {
    public static final String REGISTER = "/register";
    public static final String UPDATE = "/update";
    public static final String LOGIN = "/login";
    public static final String CHANGE_PASSWORD = "/change-password";
    public static final String USER_LIST = "/all";
    public static final String UPLOAD_PROFILE = "/upload-profile";
    public static final String REFRESH_TOKEN = "/refresh-token";
    public static final String CONFIRM_PASSWORD = "/confirm-password";
    public static final String USER_CONFIGURATION_ROOT = "/api/v1/user-configuration";
    public static final String USER_INFO_ROOT = "/api/v1/user-info";
    public static final String LIST = "/list";
    public static final String PAGEABLE_LIST = "/get-user-page-list";
    public static final String PAGEABLE_LIST_WITH_SEARCH_OBJECT = "/get-user-list";
    public static final String GET_USER_CONFIGURATION = "/get-user-configuration/{email}";
    public static final String CHANGE_STATUS = "/change-status/{email}/{status}";

    public static final String USER_OTP_BASE = "/api/v1/user-otp";
    public static final String USER_OTP_VERIFY = "/verify-otp";
    public static final String RESET_PASSWORD = "/reset-password";
    public static final String LOGIN_API_USERS = "/login/api-users";
}
