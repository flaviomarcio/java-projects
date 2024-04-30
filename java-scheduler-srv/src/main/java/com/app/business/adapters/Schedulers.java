package com.app.business.adapters;

import com.app.business.service.AgentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Slf4j
@EnableAsync
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class Schedulers {
    private final AgentService agentService;
    @Value("${scheduler.threads:1}")
    private int threads;

    @Bean
    public Schedulers schedule() {
        return this;
    }

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler pool = new ThreadPoolTaskScheduler();
        pool.setPoolSize(threads);
        pool.setThreadNamePrefix("ThreadPoolScheduler");
        return pool;
    }

    @Scheduled(fixedRate = 1000)
    public void reader() {
        agentService.start();
    }
}
