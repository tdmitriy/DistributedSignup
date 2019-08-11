package com.newage.persistenceservice.service;

import com.newage.persistenceservice.exception.ValidationException;
import com.newage.persistenceservice.model.entity.Player;
import com.newage.persistenceservice.model.event.PlayerPersistenceEvent;
import com.newage.persistenceservice.model.event.PlayerPersistenceStatus;
import com.newage.persistenceservice.model.event.PlayerSignUpEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SignUpReplyEventListener {

    private final PlayerService playerService;

    @KafkaListener(topics = "${kafka.signup-topic}", containerFactory = "kafkaJsonListenerContainerFactory")
    @SendTo
    public PlayerPersistenceEvent listen(@Payload PlayerSignUpEvent event) {
        log.info("Sign up kafka event received: '{}'", event);

        try {
            var registeredPlayer = playerService.registerPlayer(event);
            return buildResponseEvent(registeredPlayer, null);
        } catch (Exception ex) {
            return buildResponseEvent(null, ex);
        }
    }

    private PlayerPersistenceEvent buildResponseEvent(Player player, Exception ex) {
        var response = PlayerPersistenceEvent.builder();

        if (ex == null) {
            response.status(PlayerPersistenceStatus.OK)
                    .playerId(player.getId())
                    .playerEmail(player.getEmail());

            log.info("Replaying back to kafka event: {}", response.build());
            return response.build();
        }

        if (ex instanceof ValidationException) {
            response.status(PlayerPersistenceStatus.VALIDATE_ERROR).statusDescription(ex.getMessage());
            return response.build();
        } else {
            log.error("Can't build reply for PlayerPersistenceEvent: {}", ex.getMessage());
            response.status(PlayerPersistenceStatus.ERROR).statusDescription("Server error.");
            return response.build();
        }
    }
}
