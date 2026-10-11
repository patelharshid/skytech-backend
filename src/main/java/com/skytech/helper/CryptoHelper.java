package com.skytech.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

@Component
public class CryptoHelper {

    private static String appKey;
    private static String appIV;

    @Value("${APP_KEY:}")
    public void setAppKey(String appKey) {
        CryptoHelper.appKey = appKey;
    }

    @Value("${APP_IV:}")
    public void setAppIV(String appIV) {
        CryptoHelper.appIV = appIV;
    }

    public static String toBase64(String value) {
        if (StringUtils.hasText(value)) {
            return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
        }
        return value;
    }

    public static String fromBase64(String value) {
        if (StringUtils.hasText(value)) {
            byte[] decodedBytes = Base64.getDecoder().decode(value);
            return new String(decodedBytes, StandardCharsets.UTF_8);
        }
        return value;
    }

    public static String getHashKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);

        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.substring(0, 15);
    }

    public static String encryptPassword(String password, String hashKey) {
        if (!StringUtils.hasText(password) || !StringUtils.hasText(hashKey)) {
            return password;
        }
        try {
            int iterations = 10000;
            int keyLength = 256;
            char[] passwordChars = password.toCharArray();
            byte[] saltBytes = hashKey.getBytes(StandardCharsets.UTF_8);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            KeySpec spec = new PBEKeySpec(passwordChars, saltBytes, iterations, keyLength);
            byte[] hash = factory.generateSecret(spec).getEncoded();

            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    public static String encrypt(String value) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(appKey.getBytes(StandardCharsets.UTF_8), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(appIV.getBytes(StandardCharsets.UTF_8));

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);

            byte[] encryptedBytes = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
            String encryptedBase64 = Base64.getEncoder().encodeToString(encryptedBytes);

            return encryptedBase64.replace("/", "*").replace("+", "!");
        } catch (Exception e) {
            throw new RuntimeException("Error during AES encryption", e);
        }
    }

    public static String decrypt(String value) {
        try {
            String encryptedValue = value.replace("*", "/").replace("!", "+");

            SecretKeySpec secretKey = new SecretKeySpec(appKey.getBytes(StandardCharsets.UTF_8), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(appIV.getBytes(StandardCharsets.UTF_8));

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedValue));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error during AES decryption", e);
        }
    }
}
