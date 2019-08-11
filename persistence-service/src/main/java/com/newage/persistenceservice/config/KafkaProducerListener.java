package com.newage.persistenceservice.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.ProducerListener;

@Slf4j
public class KafkaProducerListener<K, V> implements ProducerListener<K, V> {

    @Override
    public void onSuccess(ProducerRecord<K, V> producerRecord, RecordMetadata recordMetadata) {
        if (log.isDebugEnabled()) {
            log.debug("Kafka message was sent: topic={}, message={} ", producerRecord.topic(), producerRecord.value());
        }
    }

    @Override
    public void onError(ProducerRecord<K, V> producerRecord, Exception exception) {
        log.error("Message sending error: topic={}, message={}, error={}", producerRecord.topic(), producerRecord.value(), exception);
    }

    @Override
    public boolean isInterestedInSuccess() {
        return false;
    }
}