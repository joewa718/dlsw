package com.dlsw.cn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.dlsw.cn.*"})
public class PromoteApplication {

	public static void main(String[] args) {
		SpringApplication.run(PromoteApplication.class, args);
	}
}
