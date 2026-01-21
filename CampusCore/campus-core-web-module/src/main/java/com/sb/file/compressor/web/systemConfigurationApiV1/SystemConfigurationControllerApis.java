package com.sb.file.compressor.web.systemConfigurationApiV1;

import com.sb.file.compressor.core.contants.BaseControllerApis;
import lombok.Data;

/**
 * @author Himal Rai on 2/8/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */
@Data
public class SystemConfigurationControllerApis extends BaseControllerApis {
    public static final String SYSTEM_CONFIGURATION_ROOT = "/api/v1/system-configuration";
    public static final String LIST = "/list";
    public static final String ID = "/{id}";
    public static final String PAGEABLE_LIST_WITH_SEARCH_OBJECT = "/get-configuration-list";
    public static final String DELETE_BY_ID = "/deleteById/{id}";

}
