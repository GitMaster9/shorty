package com.example.shorty.urlshortener;

import com.example.shorty.account.Account;
import com.example.shorty.account.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class UrlShortenerServiceTest {

    @Mock
    private UrlShortenerRepository repository;
    @Mock
    private AccountRepository accountRepository;
    private UrlShortenerService underTest;

    @BeforeEach
    void setUp() {
        underTest = new UrlShortenerService(repository, accountRepository);
    }

    @Test
    void getShortURLTestBadToken() {
        String token = "invalid-token";
        Map<String, Object> data = new HashMap<>();
        data.put("url", "www.youtube.com");

        ResponseEntity<Object> responseEntity = underTest.getShortURL(token, data);

        Map<String, Object> dataExpected = new HashMap<>();
        dataExpected.put("description", "Failed - Basic token is not valid");

        ResponseEntity<Object> expectedResponseEntity = new ResponseEntity<>(dataExpected, HttpStatus.OK);

        assertThat(responseEntity).isEqualTo(expectedResponseEntity);
    }

    @Test
    void getShortURLTestBadRequest() {
        String token = "Basicblabla";
        Map<String, Object> data = new HashMap<>();
        data.put("bla", "www.youtube.com");

        // TODO: check for account id and password in repository - for better testing
        //given(underTest.getAccountFromToken(token)).willReturn(new Account("karlo", "password"));

        ResponseEntity<Object> responseEntity = underTest.getShortURL(token, data);

        Map<String, Object> dataExpected = new HashMap<>();
        dataExpected.put("description", "Failed - no 'url' field in request body");

        ResponseEntity<Object> expectedResponseEntity = new ResponseEntity<>(dataExpected, HttpStatus.OK);

        assertThat(responseEntity).isEqualTo(expectedResponseEntity);
    }

    @Test
    void createShortSuccessResponseTest() {
        String shortUrl = "www.shorty.com/abcdefg";

        ResponseEntity<Object> responseEntity = underTest.createShortSuccessResponse(shortUrl);

        Map<String, Object> data = new HashMap<>();
        data.put("shortUrl", shortUrl);

        ResponseEntity<Object> expectedResponseEntity = new ResponseEntity<>(data, HttpStatus.OK);

        assertThat(responseEntity).isEqualTo(expectedResponseEntity);
    }

    @Test
    void createShortFailResponseTest() {
        String description = "some description";

        ResponseEntity<Object> responseEntity = underTest.createShortFailResponse(description);

        Map<String, Object> data = new HashMap<>();
        data.put("description", description);

        ResponseEntity<Object> expectedResponseEntity = new ResponseEntity<>(data, HttpStatus.OK);

        assertThat(responseEntity).isEqualTo(expectedResponseEntity);
    }

    @Disabled
    @Test
    void getStatistics() {
    }
}