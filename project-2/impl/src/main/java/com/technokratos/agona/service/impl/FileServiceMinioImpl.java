package com.technokratos.agona.service.impl;

import com.technokratos.agona.config.properties.MinioProperties;
import com.technokratos.agona.exception.file.FileStorageException;
import com.technokratos.agona.service.FileService;
import io.minio.*;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceMinioImpl implements FileService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
            String objectName = "%s.%s".formatted(UUID.randomUUID().toString(), extension);

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioProperties.getBucketName())
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            log.info("File uploaded successfully to MinIO: {}", objectName);

            return objectName;

        } catch (MinioException | IOException | GeneralSecurityException e) {
            log.error("MinIO upload error", e);
            throw new FileStorageException("Failed to upload file to MinIO", e);
        }
    }

    @Override
    public void deleteFile(String objectName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(minioProperties.getBucketName())
                    .object(objectName).build());
            log.info("Successfully deleted file {} from MinIO", objectName);
        } catch (Exception e) {
            log.error("Failed to delete file {}", objectName, e);
        }
    }
}
