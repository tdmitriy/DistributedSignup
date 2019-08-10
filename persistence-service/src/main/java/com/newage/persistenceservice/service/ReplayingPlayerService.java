package com.newage.persistenceservice.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
public class ReplayingPlayerService {
    @KafkaListener(topics = "${kafka.consumer.request-topic}")
    @SendTo
    public void xx() {

    }

}
