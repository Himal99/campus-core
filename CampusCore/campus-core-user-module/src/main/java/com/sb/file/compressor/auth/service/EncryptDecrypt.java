package com.sb.file.compressor.auth.service;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.security.SecureRandom;
import java.util.Base64;


@Component

public class EncryptDecrypt {

    private static final SecureRandom secureRandom = new SecureRandom();

    // AES-GCM parameters
    private static final int GCM_TAG_LENGTH = 128; // in bits
    private static final int IV_LENGTH = 12; // in bytes
    private static final String AES_MODE = "AES/GCM/NoPadding";
    private  final String AES_ALGORITHM = "AES";


    // Encrypt plaintext using AES-GCM and encode with Base64
    public  String encryptAndEncode(String plaintext, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_MODE);
        byte[] iv = new byte[IV_LENGTH];
        secureRandom.nextBytes(iv);
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, gcmParameterSpec);
        byte[] encryptedText = cipher.doFinal(plaintext.getBytes());
        return Base64.getEncoder().encodeToString(encryptedText);
    }


    // Decrypt ciphertext using AES-GCM
    public  String decrypt(String ciphertext, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_MODE);
        byte[] encryptedText = Base64.getDecoder().decode(ciphertext);
        byte[] iv = new byte[IV_LENGTH];
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, gcmParameterSpec);
        byte[] decryptedText = cipher.doFinal(encryptedText);
        return new String(decryptedText);
    }


}
