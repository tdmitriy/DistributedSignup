package com.newage.signupservice.model.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerPersistenceEvent {
    private UUID playerId;
    private String playerEmail;
    private PlayerPersistenceStatus status;
    private String statusDescription;
}
