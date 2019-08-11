package com.newage.persistenceservice.model.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerPersistenceEvent {
    private UUID playerId;
    private String playerEmail;
    private Instant signUpDateTime;
    private PlayerPersistenceStatus status;
    private String statusDescription;
}
