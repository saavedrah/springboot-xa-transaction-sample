package com.mono.transactionSample.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@Configuration
@ImportResource({"file:config/applicationContext.xml"})
public class DbConfig {
}
