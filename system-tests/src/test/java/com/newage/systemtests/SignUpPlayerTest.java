package com.newage.systemtests;

import com.newage.systemtests.model.dto.ErrorResponse;
import com.newage.systemtests.model.dto.SignUpRequestDto;
import com.newage.systemtests.model.dto.SignUpResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import static org.junit.Assert.*;

@Slf4j
public class SignUpPlayerTest extends AbstractSystemTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Value("${signup-service.sign-up-url}")
    private String signUpUrl;

    @Test
    public void shouldSuccessfullyDoSignUp() throws URISyntaxException {
        SignUpRequestDto testSignUp = new SignUpRequestDto("test@email.com", "123");
        HttpEntity<SignUpRequestDto> request = new HttpEntity<>(testSignUp);

        SignUpResponseDto response = doRequest(request, SignUpResponseDto.class, HttpStatus.CREATED);

        String responsePlayerId = response.getPlayerId();
        boolean playerExistsInDatabase = playerRepository.findById(UUID.fromString(responsePlayerId)).isPresent();

        assertTrue(playerExistsInDatabase);
    }

    @Test
    public void shouldReturnConflictStatusOnExistingEmail() throws URISyntaxException {
        String email = "test@email.com";
        String password = "123";

        // sign up first time to create record in database
        SignUpRequestDto testSignUp = new SignUpRequestDto(email, password);
        HttpEntity<SignUpRequestDto> request = new HttpEntity<>(testSignUp);
        SignUpResponseDto response = doRequest(request, SignUpResponseDto.class, HttpStatus.CREATED);

        String responsePlayerId = response.getPlayerId();
        boolean playerExistsInDatabase = playerRepository.findById(UUID.fromString(responsePlayerId)).isPresent();
        assertTrue(playerExistsInDatabase);

        // do sign up again with same email
        ErrorResponse errorResponse = doRequest(request, ErrorResponse.class, HttpStatus.CONFLICT);
        assertNotNull(errorResponse.getErrorMessage());
        log.info(errorResponse.getErrorMessage());

        // check records in database
        int records = playerRepository.findAll().size();
        assertEquals("Should be one record", 1, records);
    }

    @Test
    public void shouldReturnBadRequestOnEmptyEmailOrEmptyPassword() throws URISyntaxException {
        String email = "";
        String password = "";

        // sign up
        SignUpRequestDto testSignUp = new SignUpRequestDto(email, password);
        HttpEntity<SignUpRequestDto> request = new HttpEntity<>(testSignUp);

        ErrorResponse errorResponse = doRequest(request, ErrorResponse.class, HttpStatus.BAD_REQUEST);
        log.info(errorResponse.getErrorMessage());

        // check records in database
        int records = playerRepository.findAll().size();
        assertEquals("Should be 0 records", 0, records);
    }

    private <E> E doRequest(HttpEntity request, Class<E> returnedClass, HttpStatus expectedStatus) throws URISyntaxException {
        var response = testRestTemplate.postForEntity(new URI(signUpUrl), request, returnedClass);

        assertEquals(expectedStatus, response.getStatusCode());
        assertNotNull(response.getBody());
        return response.getBody();
    }
}
