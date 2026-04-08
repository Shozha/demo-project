package com.technokratos.agona.service.impl;

import com.technokratos.agona.config.MinioConfig;
import com.technokratos.agona.service.FileService;
import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceMinioImpl implements FileService {

    private final MinioClient minioClient;
    private final MinioConfig minioConfig;

    @PostConstruct
    private void initializeBucket() {
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioConfig.getBucketName()).build())) {
                log.info("Creating bucket: {}", minioConfig.getBucketName());
                minioClient.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(minioConfig.getBucketName())
                                .build()
                );
            }
        } catch (Exception e) {
            log.error("Failed to initialize MinIO bucket: {}", minioConfig.getBucketName(), e);
            throw new IllegalStateException("MinIO initialization failed", e);
        }
    }

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            String objectName = String.format("%s-%s",UUID.randomUUID(), file.getOriginalFilename());

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioConfig.getBucketName())
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(minioConfig.getBucketName())
                            .object(objectName)
                            .expiry(7, TimeUnit.DAYS)
                            .build()
            );

        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file to MinIO", e);
        }
    }
}
