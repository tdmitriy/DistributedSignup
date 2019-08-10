package com.newage.persistenceservice.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerSignUpEvent {
    private String email;
    private String password;
}
