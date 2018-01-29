package com.sam.graduation.design.gdemailserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class GdEmailServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GdEmailServerApplication.class, args);
	}
}
