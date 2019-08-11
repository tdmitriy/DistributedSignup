package com.newage.signupservice.controller;

import com.newage.signupservice.exception.ConflictException;
import com.newage.signupservice.exception.ValidationException;
import com.newage.signupservice.model.dto.SignUpRequestDto;
import com.newage.signupservice.model.dto.SignUpResponseDto;
import com.newage.signupservice.model.event.PlayerSignUpEvent;
import com.newage.signupservice.service.SignUpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/sign-up")
public class SignUpController {

    private final SignUpService signUpService;

    @PostMapping(produces = APPLICATION_JSON_UTF8_VALUE, consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignUpResponseDto> signUpPlayer(@RequestBody SignUpRequestDto dto) {
        log.info("Sign-up player REST request received: {}", dto);

        var event = new PlayerSignUpEvent(dto.getEmail(), dto.getPassword());
        var receivedEvent = signUpService.sendAndReceive(event);
        log.info("Received event: {}", receivedEvent);

        switch (receivedEvent.getStatus()) {
            case OK:
                var response = new SignUpResponseDto(receivedEvent.getPlayerId().toString());
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            case VALIDATE_ERROR:
                throw new ValidationException(receivedEvent.getStatusDescription());
            case CONFLICT_ERROR:
                throw new ConflictException(receivedEvent.getStatusDescription());
            default:
                throw new RuntimeException("Error.");
        }
    }
}
