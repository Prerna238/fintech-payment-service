package com.system.payments;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@EnableScheduling
@SpringBootApplication
public class PaymentsApplication {

	public static void main(String[] args) {

		log.info("Starting SpringBoot Application ...");
		SpringApplication.run(PaymentsApplication.class, args);
	}

}
