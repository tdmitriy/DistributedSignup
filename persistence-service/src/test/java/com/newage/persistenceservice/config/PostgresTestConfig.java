package com.newage.persistenceservice.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.sql.DataSource;

@TestConfiguration
public class PostgresTestConfig {

    @Value("${spring.liquibase.change-log}")
    private String changeLog;

    private PostgreSQLContainer container;

    @PostConstruct
    public void setUp() {
        container = new PostgreSQLContainer();
        container.start();
    }

    @PreDestroy
    public void shutdown() {
        container.stop();
    }

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder
                .create()
                .driverClassName(container.getDriverClassName())
                .url(container.getJdbcUrl())
                .username(container.getUsername())
                .password(container.getPassword())
                .build();
    }

    @Bean
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource());
        liquibase.setChangeLog(changeLog);
        return liquibase;
    }
}
