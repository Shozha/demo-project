package com.technokratos.agona.config;

import com.technokratos.agona.job.ExpiredTokensCleanupJob;
import com.technokratos.agona.job.OrphanedImagesCleanupJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    @Value("${scheduling.token-cleanup-cron:0 0 3 * * ?}")
    private String tokenCron;

    @Value("${scheduling.minio-cleanup-cron:0 0 4 * * ?}")
    private String minioCron;

    private static final String IMAGE_CLEAN_UP_JOB_NAME = "imageCleanupJob";
    private static final String TOKEN_CLEAN_UP_JOB_NAME = "tokenCleanupJob";

    @Bean
    public JobDetail imageCleanupJobDetail() {
        return JobBuilder.newJob(OrphanedImagesCleanupJob.class)
                .withIdentity(IMAGE_CLEAN_UP_JOB_NAME)
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger imageCleanupTrigger(JobDetail imageCleanupJobDetail) {
        return TriggerBuilder.newTrigger().forJob(imageCleanupJobDetail)
                .withSchedule(CronScheduleBuilder
                        .cronSchedule(minioCron))
                .build();
    }

    @Bean
    public JobDetail tokenCleanupJobDetail() {
        return JobBuilder.newJob(ExpiredTokensCleanupJob.class)
                .withIdentity(TOKEN_CLEAN_UP_JOB_NAME)
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger tokenCleanupTrigger(JobDetail tokenCleanupJobDetail) {
        return TriggerBuilder.newTrigger().forJob(tokenCleanupJobDetail)
                .withSchedule(CronScheduleBuilder
                        .cronSchedule(tokenCron))
                .build();
    }
}
