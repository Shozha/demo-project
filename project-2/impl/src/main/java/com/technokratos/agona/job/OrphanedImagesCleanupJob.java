package com.technokratos.agona.job;

import com.technokratos.agona.entity.DeletedFileLog;
import com.technokratos.agona.repository.DeletedFileLogRepository;
import com.technokratos.agona.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrphanedImagesCleanupJob extends QuartzJobBean {

    private final DeletedFileLogRepository logRepository;
    private final FileService fileService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("Starting MinIO orphaned images cleanup job...");

        Instant cutoff = Instant.now().minus(1, ChronoUnit.HOURS);

        List<DeletedFileLog> logs = logRepository.findOldLogs(cutoff, PageRequest.of(0, 50));

        if (logs.isEmpty()) {
            log.info("No orphaned images found. Cleanup finished.");
            return;
        }

        int successCount = 0;
        for (DeletedFileLog fileLog : logs) {
            String objectName = fileLog.getFileName();

            try {
                fileService.deleteFile(objectName);

                logRepository.delete(fileLog);
                successCount++;
                log.debug("Successfully cleaned up orphaned file: {}", objectName);

            } catch (Exception e) {
                if (e.getMessage() != null && (e.getMessage().contains("NoSuchKey") || e.getMessage().contains("does not exist"))) {
                    log.warn("File {} is already missing in MinIO. Removing log entry.", objectName);
                    logRepository.delete(fileLog);
                    successCount++;
                } else {
                    log.error("Failed to delete file from MinIO: {}. Will retry next time.", objectName, e);
                }
            }
        }

        log.info("MinIO cleanup finished. Successfully processed {}/{} files.", successCount, logs.size());
    }
}