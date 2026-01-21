package com.sb.file.compressor.controller;

import com.sb.file.compressor.core.contants.BaseControllerApis;

public class TokenGenerationLogControllerApis extends BaseControllerApis {
    public static final String ROOT = "/api/v1/token-generation-logs";
    public static final String GET_TOKEN_LOG_BY_USER_EMAIL= "/{email}";
    public static final String FIND_ALL = "/get-token-logs-list";
}
