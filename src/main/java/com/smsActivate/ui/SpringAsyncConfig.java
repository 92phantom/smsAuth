package com.smsActivate.ui;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class SpringAsyncConfig {

    @Bean(name = "threadPoolTaskExecutor")
    public Executor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(1000);
        taskExecutor.setMaxPoolSize(1500);
        taskExecutor.setQueueCapacity(1000);
        taskExecutor.setThreadNamePrefix("Executor-");
        taskExecutor.initialize();
        taskExecutor.setAllowCoreThreadTimeOut(true);
        return taskExecutor;
    }

}