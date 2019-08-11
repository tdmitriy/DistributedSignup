package com.newage.signupservice.service;

import com.newage.signupservice.model.event.PlayerPersistenceEvent;
import com.newage.signupservice.model.event.PlayerSignUpEvent;

public interface SignUpService {

    PlayerPersistenceEvent sendAndReceive(PlayerSignUpEvent event);
}
