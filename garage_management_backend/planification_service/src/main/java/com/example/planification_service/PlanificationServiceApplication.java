package com.example.planification_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
@SpringBootApplication
@EnableFeignClients
@ImportAutoConfiguration({FeignAutoConfiguration.class})

public class PlanificationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlanificationServiceApplication.class, args);
	}

}
