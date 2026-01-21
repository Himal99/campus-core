package com.sb.file.compressor.auth.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;

public class SecurityKeyGenerator {

    public  void generateSecretKey() throws NoSuchAlgorithmException, IOException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");

        keyGen.init(256);

        SecretKey generatedKey = keyGen.generateKey();

        byte[] keyBytes = generatedKey.getEncoded();

        // Write the file
        Files.write(Paths.get("secret.key"), keyBytes);
    }

}
