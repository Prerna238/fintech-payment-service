package com.system.payments;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
@SpringBootApplication
public class PaymentsApplication {

	public static void main(String[] args) {

		log.info("Starting SpringBoot Application ...");
		SpringApplication.run(PaymentsApplication.class, args);
	}

}
