package com.newage.persistenceservice.service;

import com.newage.persistenceservice.model.entity.Player;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PlayerServiceImpl implements PlayerService {
    @Override
    public void registerPlayer(Player player) {

    }

    @Override
    public Player findById(UUID uuid) {
        return null;
    }

    @Override
    public Player findByEmail(String email) {
        return null;
    }
}
