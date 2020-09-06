package com.springApp.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


@Configuration
@EnableAsync
public class Config {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public TaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int threadAmount = Integer.parseInt(PropertyReader.getProperties("thread.amount"));
        executor.setCorePoolSize(threadAmount);
        executor.setMaxPoolSize(threadAmount);
        executor.setThreadNamePrefix("task_executor_thread");
        executor.initialize();
        return executor;
    }
}
