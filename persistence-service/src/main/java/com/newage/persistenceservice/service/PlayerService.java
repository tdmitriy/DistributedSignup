package com.newage.persistenceservice.service;

import com.newage.persistenceservice.model.entity.Player;

import java.util.UUID;

public interface PlayerService {
    void registerPlayer(Player player);

    Player findById(UUID uuid);

    Player findByEmail(String email);
}
