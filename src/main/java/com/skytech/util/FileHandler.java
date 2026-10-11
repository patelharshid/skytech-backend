package com.skytech.util;

import com.skytech.error.CustomException;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class FileHandler {

    public static File convertMultipartFileToFile(MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        try {
            Path tempFile = Files.createTempFile("skytech-upload-", "-" + safeFilename(originalFilename));
            multipartFile.transferTo(tempFile.toFile());
            return tempFile.toFile();
        } catch (IOException e) {
            throw new CustomException("image", "Could not process uploaded image");
        }
    }

    public static String generateUniqueS3Key(MultipartFile file) {
        return UUID.randomUUID().toString().substring(0, 6) + "_" + safeFilename(file.getOriginalFilename());
    }

    private static String safeFilename(String filename) {
        String name = StringUtils.cleanPath(filename == null || filename.isBlank() ? "image" : filename);
        return name.replaceAll("[^A-Za-z0-9._-]", "_");
    }
}
