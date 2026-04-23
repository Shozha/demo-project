package com.technokratos.agona.job;

import com.technokratos.agona.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExpiredTokensCleanupJob extends QuartzJobBean {

    private final RefreshTokenService refreshTokenService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("Quartz [TokenCleanupJob]: Starting cleanup of expired and revoked tokens...");
        try {
            int deleted = refreshTokenService.cleanupTokens();
            log.info("Quartz [TokenCleanupJob]: Successfully deleted {} tokens.", deleted);
        } catch (Exception e) {
            log.error("Quartz [TokenCleanupJob]: Error during token cleanup", e);
            throw new JobExecutionException(e);
        }
    }
}
