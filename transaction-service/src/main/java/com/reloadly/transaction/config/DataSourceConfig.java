package com.reloadly.transaction.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Slf4j
public class DataSourceConfig {

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.url}")
    private String url;

    @Profile("dev")
    @Bean
    public void devDbConnection() {
        log.info("Setting up DB connection for DEV with url: {}, driverClass: {}", url, driverClassName);
    }

    @Profile("prod")
    @Bean
    public void prodDbConnection() {
        log.info("Setting up DB connection for PROD with url: {}, driverClass: {}", url, driverClassName);
    }
}
