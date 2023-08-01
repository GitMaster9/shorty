package com.example.shorty.urlshortener;

import com.example.shorty.repository.UrlShortenerRepository;
import com.example.shorty.restapi.Account;
import com.example.shorty.repository.AccountRepository;
import com.example.shorty.restapi.UrlShortener;
import com.example.shorty.service.UrlShortenerService;
import com.example.shorty.utils.TokenEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.lang.Character.isDigit;
import static java.lang.Character.isLetter;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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

        String shortUrl = underTest.getShortURL(token, data);

        assertThat(shortUrl).isEqualTo(null);
    }

    @Test
    void getShortURLTestBadRequest() {
        String accountId = "karlo";
        String password = "password";
        String token = TokenEncoder.getBasicAuthorizationToken(accountId, password);
        Map<String, Object> data = new HashMap<>();
        data.put("bla", "www.youtube.com");

        given(accountRepository.findByAccountIdAndPassword(accountId, password)).willReturn(new Account(accountId, password));
        String shortUrl = underTest.getShortURL(token, data);

        assertThat(shortUrl).isEqualTo(null);
    }

    @Test
    void getShortURLTestSuccess1() {
        String accountId = "karlo";
        String password = "password";
        String token = TokenEncoder.getBasicAuthorizationToken(accountId, password);
        Map<String, Object> data = new HashMap<>();
        data.put("url", "www.youtube.com");
        data.put("redirectType", 301);

        given(accountRepository.findByAccountIdAndPassword(accountId, password)).willReturn(new Account(accountId, password));

        String shortUrl = underTest.getShortURL(token, data);

        int urlSize = shortUrl.length();
        assertThat(urlSize).isEqualTo(7);

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
    void getShortURLTestSuccess2() {
        String accountId = "karlo";
        String password = "password";
        String token = TokenEncoder.getBasicAuthorizationToken(accountId, password);
        Map<String, Object> data = new HashMap<>();
        data.put("url", "www.youtube.com");

        given(accountRepository.findByAccountIdAndPassword(accountId, password)).willReturn(new Account(accountId, password));
        String shortUrl = underTest.getShortURL(token, data);

        int urlSize = shortUrl.length();
        assertThat(urlSize).isEqualTo(7);

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
    void createShortResponseTest() {
        String shortUrl = "www.shorty.com/abcdefg";
        ResponseEntity<Object> responseEntity = underTest.createShortResponse(true, shortUrl);

        Map<String, Object> data = new HashMap<>();
        data.put("shortUrl", shortUrl);
        ResponseEntity<Object> expectedResponseEntity = new ResponseEntity<>(data, HttpStatus.OK);

        assertThat(responseEntity).isEqualTo(expectedResponseEntity);

        String description = "Failed because of XYZ!";
        responseEntity = underTest.createShortResponse(false, description);

        data = new HashMap<>();
        data.put("description", description);
        expectedResponseEntity = new ResponseEntity<>(data, HttpStatus.OK);

        assertThat(responseEntity).isEqualTo(expectedResponseEntity);
    }

    @Test
    void getUniqueURLsTest() {
        List<UrlShortener> allURLs = new ArrayList<>();

        UrlShortener test1 = new UrlShortener("www.google.com", "dummy1", "karlo", 302, 1);
        UrlShortener test2 = new UrlShortener("www.google.com", "dummy2", "karlo", 302, 2);
        UrlShortener test3 = new UrlShortener("www.youtube.com", "dummy3", "karlo", 302, 4);
        UrlShortener test4 = new UrlShortener("www.youtube.com", "dummy4", "karlo", 302, 5);
        UrlShortener test5 = new UrlShortener("www.facebook.com", "dummy5", "karlo", 302, 7);
        UrlShortener test6 = new UrlShortener("www.facebook.com", "dummy6", "karlo", 302, 20);
        allURLs.add(test1);
        allURLs.add(test2);
        allURLs.add(test3);
        allURLs.add(test4);
        allURLs.add(test5);
        allURLs.add(test6);

        List<UrlShortener> uniqueURLs = underTest.getUniqueURLs(allURLs);

        List<UrlShortener> expectedURLs = new ArrayList<>();
        UrlShortener unique1 = new UrlShortener("www.google.com", "karlo", 3);
        UrlShortener unique2 = new UrlShortener("www.youtube.com", "karlo", 9);
        UrlShortener unique3 = new UrlShortener("www.facebook.com", "karlo", 27);
        expectedURLs.add(unique1);
        expectedURLs.add(unique2);
        expectedURLs.add(unique3);

        assertThat(uniqueURLs).usingRecursiveComparison().isEqualTo(expectedURLs);
    }

    @Test
    void GetStatisticsTestFail() {
        String accountId = "karlo";
        String password = "password";
        String token = TokenEncoder.getBasicAuthorizationToken(accountId, password);

        ResponseEntity<Object> responseEntity = underTest.getStatistics(token);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void getStatisticsTest() {
        String accountId = "karlo";
        String password = "password";
        String token = TokenEncoder.getBasicAuthorizationToken(accountId, password);

        List<UrlShortener> allURLs = getAllURLsExample();

        given(accountRepository.findByAccountIdAndPassword(accountId, password)).willReturn(new Account(accountId, password));
        given(repository.findByAccountId(accountId)).willReturn(allURLs);

        ResponseEntity<Object> responseEntity = underTest.getStatistics(token);

        List<UrlShortener> expectedURLs = getUniqueURLsExample();
        Map<String, Object> data = new HashMap<>();

        for (UrlShortener unique : expectedURLs) {
            String url = unique.getUrl();
            int redirects = unique.getRedirects();
            data.put(url, redirects);
        }

        ResponseEntity<Object> expectedResponseEntity = new ResponseEntity<>(data, HttpStatus.OK);

        assertThat(responseEntity).isEqualTo(expectedResponseEntity);
    }

    List<UrlShortener> getAllURLsExample() {
        List<UrlShortener> allURLs = new ArrayList<>();

        UrlShortener test1 = new UrlShortener("www.google.com", "dummy1", "karlo", 302, 1);
        UrlShortener test2 = new UrlShortener("www.google.com", "dummy2", "karlo", 302, 2);
        UrlShortener test3 = new UrlShortener("www.youtube.com", "dummy3", "karlo", 302, 4);
        UrlShortener test4 = new UrlShortener("www.youtube.com", "dummy4", "karlo", 302, 5);
        UrlShortener test5 = new UrlShortener("www.facebook.com", "dummy5", "karlo", 302, 7);
        UrlShortener test6 = new UrlShortener("www.facebook.com", "dummy6", "karlo", 302, 20);
        allURLs.add(test1);
        allURLs.add(test2);
        allURLs.add(test3);
        allURLs.add(test4);
        allURLs.add(test5);
        allURLs.add(test6);

        return allURLs;
    }

    List<UrlShortener> getUniqueURLsExample() {
        List<UrlShortener> expectedURLs = new ArrayList<>();
        UrlShortener unique1 = new UrlShortener("www.google.com", "karlo", 3);
        UrlShortener unique2 = new UrlShortener("www.youtube.com", "karlo", 9);
        UrlShortener unique3 = new UrlShortener("www.facebook.com", "karlo", 27);
        expectedURLs.add(unique1);
        expectedURLs.add(unique2);
        expectedURLs.add(unique3);

        return expectedURLs;
    }
}