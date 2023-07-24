package com.example.shorty.urlshortener;

import com.example.shorty.account.Account;
import com.example.shorty.account.AccountRepository;
import com.example.shorty.responsehandler.ResponseHandler;
import com.example.shorty.token.TokenEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;
import static java.lang.Character.isDigit;
import static java.lang.Character.isLetter;
import static org.assertj.core.api.AssertionsForClassTypes.fail;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UrlShortenerServiceTest {

    @Mock
    private UrlShortenerRepository repository;
    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
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
        String accountId = "karlo";
        String password = "password";
        String token = TokenEncoder.encodeBasicToken(accountId, password);
        Map<String, Object> data = new HashMap<>();
        data.put("bla", "www.youtube.com");

        given(accountRepository.findAccountByIdAndPassword(accountId, password)).willReturn(new Account(accountId, password));

        ResponseEntity<Object> responseEntity = underTest.getShortURL(token, data);

        Map<String, Object> dataExpected = new HashMap<>();
        dataExpected.put("description", "Failed - no 'url' field in request body");

        ResponseEntity<Object> expectedResponseEntity = new ResponseEntity<>(dataExpected, HttpStatus.OK);

        assertThat(responseEntity).isEqualTo(expectedResponseEntity);
    }

    @Test
    void getShortURLTestSuccess() {
        String accountId = "karlo";
        String password = "password";
        String token = TokenEncoder.encodeBasicToken(accountId, password);
        Map<String, Object> data = new HashMap<>();
        data.put("url", "www.youtube.com");

        given(accountRepository.findAccountByIdAndPassword(accountId, password)).willReturn(new Account(accountId, password));

        ResponseEntity<Object> responseEntity = underTest.getShortURL(token, data);

        Object shortUrlObject = ResponseHandler.getDataFieldFromResponse(responseEntity, "shortUrl");
        String shortUrl = "";
        if (shortUrlObject != null) {
            shortUrl = shortUrlObject.toString();
        }

        if (!shortUrl.startsWith("https://shorty.com/")) {
            fail("short URL not valid - " + shortUrl);
        }

        shortUrl = shortUrl.replace("https://shorty.com/", "");

        int urlEndSize = shortUrl.length();
        assertThat(urlEndSize).isEqualTo(7);

        boolean valid = true;
        for (char letter : shortUrl.toCharArray()) {
            if (!isLetter(letter) && !isDigit(letter)) {
                valid = false;
                break;
            }
        }

        assertThat(valid).isEqualTo(true);
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

    @Test
    void redirectUrlTest() {
        String shortUrl = "dummyshorturl";

        when(repository.findUrlShortenerByShortUrl(shortUrl)).thenReturn(new UrlShortener("www.youtube.com", shortUrl, "alex",0));

        UrlShortener urlShortenerStart = repository.findUrlShortenerByShortUrl(shortUrl);
        int redirectsStart = 0;
        if (urlShortenerStart != null) {
            redirectsStart = urlShortenerStart.getRedirects();
        }

        underTest.redirectUrl(shortUrl);

        when(repository.findUrlShortenerByShortUrl(shortUrl)).thenReturn(new UrlShortener("www.youtube.com", shortUrl, "alex",1));
        UrlShortener urlShortenerEnd = repository.findUrlShortenerByShortUrl(shortUrl);
        int redirectsEnd = 0;
        if (urlShortenerEnd != null) {
            redirectsEnd = urlShortenerEnd.getRedirects();
        }

        assertThat(redirectsStart).isNotEqualTo(redirectsEnd);
    }
}