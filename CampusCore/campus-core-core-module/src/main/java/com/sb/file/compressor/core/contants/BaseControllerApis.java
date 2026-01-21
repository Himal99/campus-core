package com.sb.file.compressor.core.contants;

import lombok.Data;

/**
 * @author Himal Rai on 2/22/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */
@Data
public class BaseControllerApis {
    public static final String LIST = "/list";
    public static final String ID = "/{id}";
    public static final String DELETE_BY_ID = "/deleteById/{id}";
}
