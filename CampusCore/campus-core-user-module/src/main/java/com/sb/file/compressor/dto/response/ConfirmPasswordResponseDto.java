package com.sb.file.compressor.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Himal Rai on 2/5/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmPasswordResponseDto {
    private boolean validate;
}
