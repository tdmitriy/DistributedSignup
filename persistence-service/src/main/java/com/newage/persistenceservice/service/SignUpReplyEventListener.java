package com.newage.persistenceservice.service;

import com.newage.persistenceservice.model.entity.Player;
import com.newage.persistenceservice.model.event.PlayerPersistenceEvent;
import com.newage.persistenceservice.model.event.PlayerPersistenceStatus;
import com.newage.persistenceservice.model.event.PlayerSignUpEvent;
import com.newage.persistenceservice.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Component
@RequiredArgsConstructor
public class SignUpReplyEventListener {

    private final PlayerRepository playerRepository;

    @Transactional(value = "chainedKafkaTransactionManager")
    @KafkaListener(topics = "${kafka.signup-topic}", containerFactory = "kafkaJsonListenerContainerFactory")
    @SendTo
    public PlayerPersistenceEvent listen(@Payload PlayerSignUpEvent event) {
        log.info("Sign up kafka event received: '{}'", event);

        Player player = Player.builder()
                .email(event.getEmail())
                .password(event.getPassword()) // TODO encrypt
                .build();

        return savePlayer(player);
    }

    private PlayerPersistenceEvent savePlayer(Player player) {
        var response = PlayerPersistenceEvent.builder();

        if (playerRepository.findByEmail(player.getEmail()).isPresent()) {
            response.status(PlayerPersistenceStatus.VALIDATE_ERROR).statusDescription("Player with given email: " + player.getEmail() + " already exists.");
            return response.build();
        } else {
            Player saved = playerRepository.save(player);
            response.status(PlayerPersistenceStatus.OK)
                    .playerId(saved.getId())
                    .playerEmail(saved.getEmail());

            log.info("Replaying back to kafka event: {}", response.build());
            return response.build();
        }
    }
}
