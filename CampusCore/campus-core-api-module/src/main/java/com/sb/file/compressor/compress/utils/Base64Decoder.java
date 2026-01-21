package com.sb.file.compressor.compress.utils;

import java.util.Base64;

/**
 * @author Himal Rai on 1/23/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */
public class Base64Decoder {
    public static byte[] decodeBase64(String base64Data) {
        return Base64.getDecoder().decode(base64Data);
    }
}
