package com.newage.signupservice.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerPersistenceEvent {
    private UUID playerId;
    private String playerEmail;
    private Instant signUpDateTime;
    private PlayerPersistenceStatus status;
    private String statusDescription;
}
