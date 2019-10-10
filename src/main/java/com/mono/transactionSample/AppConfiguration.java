package com.mono.transactionSample;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
//@EnableConfigurationProperties(MyProperties.class)
@ImportResource({"file:config/applicationContext.xml"})
public class AppConfiguration {

    public static String dataSourceName = "application";

//    public String driverClass = "org.postgresql.Driver";
    public String driverClass = "org.postgresql.xa.PGXADataSource";
//    public String url = "jdbc:postgresql://localhost:5432/scuserapp";
//    public String username = "scuserapp";
//    public String password = "scuserapppw";


    @Bean
    public DataSource dataSource(){
//        System.out.println(driverClass+" "+ url+" "+username+" "+password);
//        DriverManagerDataSource source = new DriverManagerDataSource();
        org.apache.commons.dbcp.BasicDataSource source = new org.apache.commons.dbcp.BasicDataSource();
        source.setDriverClassName(driverClass);
//        source.setUrl(url);
//        source.setUsername(username);
//        source.setPassword(password);
//        source.setInitialSize(10);
//        source.setMaxActive(20);
//        source.setMaxOpenPreparedStatements(5);
        return source;
    }

//    @Bean
//    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(){
//        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(this.dataSource());
//        return namedParameterJdbcTemplate;
//    }
}
