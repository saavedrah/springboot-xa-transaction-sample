package com.mono.transactionSample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;

@SpringBootApplication
@ConfigurationProperties
public class TransactionSampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionSampleApplication.class, args);
	}

}
