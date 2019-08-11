package com.newage.persistenceservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;

@SpringBootApplication(exclude = KafkaAutoConfiguration.class)
public class PersistenceServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PersistenceServiceApplication.class, args);
    }

}
