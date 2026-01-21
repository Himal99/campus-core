package com.sb.file.compressor.controller;

import com.sb.file.compressor.core.contants.BaseControllerApis;

public class CompressActivityLogControllerApis extends BaseControllerApis {
    public static final String ROOT = "/api/v1/compress-activity-logs";
    public static final String GET_COMPRESS_LOG_BY_USER_EMAIL= "/{email}";
    public static final String PAGEABLE = "/get-compress-logs-list";
}
