package com.newage.signupservice.service;

import com.newage.signupservice.model.event.PlayerPersistenceEvent;
import com.newage.signupservice.model.event.PlayerPersistenceStatus;
import com.newage.signupservice.model.event.PlayerSignUpEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignUpServiceImpl implements SignUpService {

    private final ReplyingKafkaTemplate<String, PlayerSignUpEvent, PlayerPersistenceEvent> replyKafkaTemplate;

    @Value("${kafka.signup-topic}")
    private String signUpTopic;
    @Value("${kafka.reply-topic}")
    private String replyTopic;

    @Override
//    @Transactional
    public PlayerPersistenceEvent sendAndReceive(PlayerSignUpEvent event) {
        try {
            ProducerRecord<String, PlayerSignUpEvent> record = new ProducerRecord<>(signUpTopic, event);
            record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, replyTopic.getBytes()));

            var consumerRecord = replyKafkaTemplate.sendAndReceive(record).get();
            log.info("Received kafka reply: {}", consumerRecord.value());

            return consumerRecord.value();
        } catch (Exception ex) {
            log.error("Can't send or receive kafka reply event. {}", ex.getMessage());
            return PlayerPersistenceEvent.builder()
                    .status(PlayerPersistenceStatus.ERROR)
                    .statusDescription("Can't send or receive kafka reply event, internal error.")
                    .build();
        }
    }
}
