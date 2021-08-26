package com.everis.currentaccount;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableEurekaClient
@SpringBootApplication
public class CurrentAccountServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CurrentAccountServiceApplication.class, args);  
		log.info("Servicio de credito activado.");
	}

}
