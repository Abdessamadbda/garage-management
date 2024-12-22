package com.example.facture_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import	org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;



@SpringBootApplication
@EnableFeignClients
@ImportAutoConfiguration({FeignAutoConfiguration.class})
public class FactureServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FactureServiceApplication.class, args);
	}

}