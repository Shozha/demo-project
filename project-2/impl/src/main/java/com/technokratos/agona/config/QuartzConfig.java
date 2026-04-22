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
    private String tokenCleanupCron;

    private static final String IMAGE_CLEAN_UP_JOB_NAME = "imageCleanupJob";
    private static final String TOKEN_CLEAN_UP_JOB_NAME = "tokenCleanupJob";

    private static final int IMAGE_CLEAN_UP_INTERVAL = 1;

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
                .withSchedule(SimpleScheduleBuilder
                        .simpleSchedule().withIntervalInHours(IMAGE_CLEAN_UP_INTERVAL).repeatForever())
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
                        .cronSchedule(tokenCleanupCron))
                .build();
    }
}
