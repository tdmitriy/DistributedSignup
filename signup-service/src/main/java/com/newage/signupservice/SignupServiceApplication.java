package com.newage.signupservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;

@SpringBootApplication(exclude = KafkaAutoConfiguration.class)
public class SignupServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SignupServiceApplication.class, args);
    }

}
