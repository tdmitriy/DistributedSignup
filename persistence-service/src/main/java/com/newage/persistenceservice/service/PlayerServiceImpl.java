package com.newage.persistenceservice.service;

import com.newage.persistenceservice.exception.ValidationException;
import com.newage.persistenceservice.model.entity.Player;
import com.newage.persistenceservice.model.event.PlayerSignUpEvent;
import com.newage.persistenceservice.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    @Override
    public Player registerPlayer(PlayerSignUpEvent event) {

        if (findByEmail(event.getEmail()).isPresent())
            throw new ValidationException("Player with given email: " + event.getEmail() + " already registered.");

        Player player = Player.builder()
                .email(event.getEmail())
                .password(event.getPassword()) // TODO encrypt
                .build();

        return playerRepository.save(player);
    }

    @Override
    public Optional<Player> findById(UUID uuid) {
        return playerRepository.findById(uuid);
    }

    @Override
    public Optional<Player> findByEmail(String email) {
        return playerRepository.findByEmail(email);
    }
}
