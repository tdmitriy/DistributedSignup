package com.newage.signupservice.controller;

import com.newage.signupservice.exception.ValidationException;
import com.newage.signupservice.model.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> globalExceptionHandler(Exception ex) {
        log.error("Global web exception", ex);
        ErrorResponse response = new ErrorResponse("Internal server error.");

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> entityValidationHandler(ValidationException ex) {
        log.error("Validation exception. {}", ex.getMessage());
        ErrorResponse response = new ErrorResponse(String.format("Validation exception: %s", ex.getMessage()));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> jsonDeserializationHandler(Exception ex) {
        String errorMessage = "Json validation exception";
        log.error(errorMessage, ex);

        ErrorResponse response = new ErrorResponse(errorMessage);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> mediaTypeNotSupportedHandler(HttpMediaTypeNotSupportedException ex) {
        String errorMessage = "Unsupported content type: " + ex.getContentType();
        log.error(errorMessage, ex);

        ErrorResponse response = new ErrorResponse(errorMessage);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }
}