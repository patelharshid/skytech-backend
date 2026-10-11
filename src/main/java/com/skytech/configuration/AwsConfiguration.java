package com.skytech.configuration;

import com.skytech.error.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.File;
import java.time.Duration;

@Slf4j
@Configuration
public class AwsConfiguration {

    @Value("${aws.access-key:}")
    private String accessKey;

    @Value("${aws.secret-key:}")
    private String secretKey;

    @Value("${aws.region:ap-southeast-2}")
    private String region;

    @Value("${aws.s3.bucket-name:}")
    private String bucketName;

    private S3Client client;
    private S3Presigner presigner;

    @Bean
    public S3Client s3Client() {
        client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(credentialsProvider())
                .build();
        return client;
    }

    @Bean
    public S3Presigner s3Presigner() {
        presigner = S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(credentialsProvider())
                .build();
        return presigner;
    }

    private software.amazon.awssdk.auth.credentials.AwsCredentialsProvider credentialsProvider() {
        if (StringUtils.hasText(accessKey) && StringUtils.hasText(secretKey)) {
            return StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey));
        }
        return DefaultCredentialsProvider.create();
    }

    public void uploadFile(String keyName, File file) {
        putFile(keyName, file, null);
    }

    public void uploadFile(String keyName, File file, String contentType) {
        putFile(keyName, file, contentType);
    }

    public void updateFile(String keyName, File file) {
        putFile(keyName, file, null);
    }

    private void putFile(String keyName, File file, String contentType) {
        ensureBucketConfigured();
        try {
            PutObjectRequest.Builder builder = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName);
            if (StringUtils.hasText(contentType)) builder.contentType(contentType);
            PutObjectRequest request = builder.build();
            client.putObject(request, RequestBody.fromFile(file));
            log.info("Uploaded file to S3: bucket={}, key={}", bucketName, keyName);
        } catch (Exception e) {
            log.error("Failed to upload S3 file: key={}", keyName, e);
            throw new CustomException("aws", "Failed to upload file to S3");
        }
    }

    public void deleteFile(String key) {
        ensureBucketConfigured();
        try {
            client.deleteObject(DeleteObjectRequest.builder().bucket(bucketName).key(key).build());
        } catch (Exception e) {
            log.error("Failed to delete S3 file: key={}", key, e);
            throw new CustomException("aws", "Failed to delete file from S3");
        }
    }

    public String getPresignedUrl(String key) {
        if (!StringUtils.hasText(key)) return null;
        ensureBucketConfigured();
        try {
            GetObjectPresignRequest request = GetObjectPresignRequest.builder()
                    .getObjectRequest(GetObjectRequest.builder().bucket(bucketName).key(key).build())
                    .signatureDuration(Duration.ofHours(1))
                    .build();
            return presigner.presignGetObject(request).url().toString();
        } catch (Exception e) {
            log.error("Failed to create S3 presigned URL: key={}", key, e);
            throw new CustomException("aws", "Failed to create image URL");
        }
    }

    private void ensureBucketConfigured() {
        if (!StringUtils.hasText(bucketName)) {
            throw new CustomException("aws", "AWS_S3_BUCKET_NAME is not configured");
        }
    }

}
