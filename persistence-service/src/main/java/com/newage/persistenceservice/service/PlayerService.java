package com.newage.persistenceservice.service;

import com.newage.persistenceservice.exception.ValidationException;
import com.newage.persistenceservice.model.entity.Player;
import com.newage.persistenceservice.model.event.PlayerSignUpEvent;

import java.util.Optional;
import java.util.UUID;

public interface PlayerService {
    Player registerPlayer(PlayerSignUpEvent event) throws ValidationException;

    Optional<Player> findById(UUID uuid);

    Optional<Player> findByEmail(String email);
}
