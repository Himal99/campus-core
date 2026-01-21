package com.sb.file.compressor.web.compressApiV1;

import lombok.Data;

/**
 * @author Himal Rai on 1/30/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */

@Data
public class CompressControllerApis {

    public static final String COMPRESS_ROOT = "/api/v1/compress";
    public static final String COMPRESS_FILES = "/compressFiles";
    public static final String SERVER_COMPRESS = "/server-compress";
    public static final String DOWNLOAD_SINGLE_FILE = "/downloadSingleFile";
    public static final String DOWNLOAD_ALL_FILE = "/downloadAllFile";
    public static final String COMPRESS_NOTIFICATION_API = "/notification-api";
    public static final String COMPRESS_VIDEO = "/compress-video";
    public static final String CALL_BACK = "/callback";
    public static final String LOG_REPORTS = "/reports/all";

}
