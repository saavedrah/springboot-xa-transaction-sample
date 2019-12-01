package com.mono.transactionSample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ConfigurationProperties
@EnableJpaRepositories
public class TransactionSampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionSampleApplication.class, args);
	}

}
