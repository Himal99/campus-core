package com.sb.file.compressor.core.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Himal Rai on 1/14/2024
 * Sb Solutions Nepal pvt.ltd
 * Project sb-back-core.
 */
@Data
@AllArgsConstructor@NoArgsConstructor@Builder
public class ResponseModel {

    private int status;
    private String message;
    private Object data;
}
