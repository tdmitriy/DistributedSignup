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

import static org.springframework.util.StringUtils.isEmpty;


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

        if (isEmpty(player.getEmail()) || isEmpty(player.getPassword())) {
            response.status(PlayerPersistenceStatus.VALIDATE_ERROR).statusDescription("Email and password are required.");
            return response.build();
        }

        if (playerRepository.findByEmail(player.getEmail()).isPresent()) {
            response.status(PlayerPersistenceStatus.VALIDATE_ERROR)
                    .statusDescription(String.format("Player with given email: '%s' already exists.", player.getEmail()));
            return response.build();
        }

        Player saved = playerRepository.save(player);
        response.status(PlayerPersistenceStatus.OK)
                .playerId(saved.getId())
                .playerEmail(saved.getEmail());

        log.info("Replaying back to kafka event: {}", response.build());
        return response.build();
    }
}
