package com.sb.file.compressor.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;
@Slf4j
public class ApiUserTokenGenerator {
    private final static SecureRandom secureRandom = new SecureRandom();
    private final static int GCM_IV_LENGTH = 12;

    public static String encrypt(String plaintext) throws Exception {

        byte[] keyBytes = loadSecretKey();
        SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");
        byte[] associatedData = "SBSOLUTIONSNEPAL".getBytes(StandardCharsets.UTF_8);
        String message = plaintext;

        byte[] iv = new byte[GCM_IV_LENGTH];
        secureRandom.nextBytes(iv);
        final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);

        if (associatedData != null) {
            cipher.updateAAD(associatedData);
        }

        byte[] cipherText = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + cipherText.length);
        byteBuffer.put(iv);
        byteBuffer.put(cipherText);
        return Base64.getEncoder().encodeToString(byteBuffer.array());
    }

    public static String decrypt(String cipherMessage) throws Exception {

        byte[] keyBytes = loadSecretKey();
        SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");
        byte[] associatedData = "SBSOLUTIONSNEPAL".getBytes(StandardCharsets.UTF_8);

        byte[] decodedCipherText = Base64.getDecoder().decode(cipherMessage);
        final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        AlgorithmParameterSpec gcmIv = new GCMParameterSpec(128, decodedCipherText, 0, GCM_IV_LENGTH);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmIv);

        if (associatedData != null) {
            cipher.updateAAD(associatedData);
        }

        byte[] plainText = cipher.doFinal(decodedCipherText, GCM_IV_LENGTH, decodedCipherText.length - GCM_IV_LENGTH);

        return new String(plainText, StandardCharsets.UTF_8);
    }

    public static byte[] loadSecretKey() {
        try {
            Resource resource = new ClassPathResource("secret.key");
            return IOUtils.toByteArray(resource.getInputStream());
        } catch (IOException e) {
            log.error("Error in accessing the secret key", e);
        }
        return null;
    }
}
