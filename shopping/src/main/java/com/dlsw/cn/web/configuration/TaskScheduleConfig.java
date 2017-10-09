package com.dlsw.cn.web.configuration;

import com.dlsw.cn.web.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableScheduling
@Component
public class TaskScheduleConfig extends BaseService{
    @Autowired
    RestTemplate restTemplate ;
    @Value("${flush.path}")
    String url;
    private final static Logger log = LoggerFactory.getLogger(TaskScheduleConfig.class);
    @Scheduled(fixedRate = 1000 * 60 * 15)
    public void cronJob() {
        log.debug("upgradeUerRoleType begin");
        restTemplate.getForEntity(url,null);
        log.debug("upgradeUerRoleType end");
    }

    @Bean
    public TaskScheduler taskScheduler() {
        return new ConcurrentTaskScheduler(); //single threaded by default
    }
} 