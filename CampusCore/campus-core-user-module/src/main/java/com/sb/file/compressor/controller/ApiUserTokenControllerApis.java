package com.sb.file.compressor.controller;

import com.sb.file.compressor.core.contants.BaseControllerApis;

public class ApiUserTokenControllerApis extends BaseControllerApis {
    public static final String ROOT = "/api/v1/token-generator";
    public static final String GET_TOKEN_BY_ID= "/{id}";
    public static final String UPDATE_TOKEN = "/update";
    public static final String LIST = "/list";
    public static final String PAGEABLE = "/get-api-user-token-list";
    public static final String PAGEABLE_BY_EMAIL = "/get-api-user-token/{email}";
    public static final String DELETE_TOKEN = "/delete/{id}";
}
