package com.dlsw.cn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = {"com.dlsw.cn.*"})
public class CommonApplication{
    private static final Logger logger = LoggerFactory.getLogger(CommonApplication.class);

    public static void main(String[] args) throws Exception {
        SpringApplication.run(CommonApplication.class, args);
    }

}