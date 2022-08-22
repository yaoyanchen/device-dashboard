package com.easy.dashboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ThreadPoolConfig {
    // 核心线程池大小
    private final int corePoolSize = 500;

    // 最大可创建的线程数
    private final int maxPoolSize = 2000;

    // 队列最大长度
    private final int queueCapacity = 10000;

    // 线程池维护线程所允许的空闲时间
    private final int keepAliveSeconds = 3000;

    @Bean(name = "threadPoolTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor()
    {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(maxPoolSize);
        executor.setCorePoolSize(corePoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        // 线程池对拒绝任务(无线程可用)的处理策略
        //  executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        return executor;
    }

}
