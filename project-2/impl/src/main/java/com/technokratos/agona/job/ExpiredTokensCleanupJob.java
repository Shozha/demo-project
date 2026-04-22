package com.technokratos.agona.job;

import com.technokratos.agona.service.impl.RefreshTokenService;
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

    private final RefreshTokenService tokenService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("Starting scheduled cleanup of refresh tokens...");
        int total = tokenService.cleanupTokens();
        log.debug("Successfully deleted {} expired/revoked tokens.", total);
    }
}
