package com.dlsw.cn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
//@EnableScheduling
@ServletComponentScan
@SpringBootApplication
@ComponentScan(basePackages = {"com.dlsw.cn.shopping.*","com.dlsw.cn.common.*"})
public class ShoppingApplication extends SpringBootServletInitializer {
    private static final Logger logger = LoggerFactory.getLogger(ShoppingApplication.class);

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ShoppingApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ShoppingApplication.class);
    }

}