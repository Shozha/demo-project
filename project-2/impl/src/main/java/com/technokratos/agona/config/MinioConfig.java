package com.technokratos.agona.config;

import com.technokratos.agona.config.properties.MinioProperties;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.SetBucketPolicyArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MinioConfig {

    private final MinioProperties minioProperties;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(minioProperties.getUrl())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();
    }

    @Bean
    public ApplicationRunner initMinioBucket(MinioClient minioClient) {
        return args -> {
            try {
                String bucketName = minioProperties.getBucketName();

                if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                    log.info("Creating bucket: {}", bucketName);
                    minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                    log.info("MinIO bucket '{}' created successfully", bucketName);

                    minioClient.setBucketPolicy(
                            SetBucketPolicyArgs.builder()
                                    .bucket(bucketName)
                                    .config(getPolicy(bucketName))
                                    .build()
                    );
                    log.info("Public read policy configured for bucket '{}'", bucketName);
                } else {
                    log.info("MinIO bucket '{}' already exists", bucketName);
                }
            } catch (Exception e) {
                throw new IllegalStateException("MinIO initialization failed", e);
            }
        };
    }

    private String getPolicy(String bucketName) {
        return """
                {
                  "Version": "2012-10-17",
                  "Statement": [
                    {
                      "Action": ["s3:GetObject"],
                      "Effect": "Allow",
                      "Principal": {"AWS": ["*"]},
                      "Resource": ["arn:aws:s3:::%s/*"]
                    }
                  ]
                }
                """.formatted(bucketName);
    }
}
