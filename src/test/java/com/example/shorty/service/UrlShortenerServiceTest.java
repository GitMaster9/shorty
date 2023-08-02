package com.example.shorty.service;

import com.example.shorty.example.UrlShortenerExample;
import com.example.shorty.repository.UrlShortenerRepository;
import com.example.shorty.restapi.Account;
import com.example.shorty.repository.AccountRepository;
import com.example.shorty.restapi.UrlShortener;
import com.example.shorty.utils.StringGenerator;
import com.example.shorty.utils.TokenEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
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
    void getShortURLTest() {
        String accountId = "karlo";
        String password = "password";
        String url = "www.youtube.com";
        int redirectType = 301;

        Account account = new Account();
        account.setAccountId(accountId);
        account.setPassword(password);

        String shortUrl = underTest.shortURL(account, url, redirectType);
        assertThat(shortUrl).isNotNull();

        int urlSize = shortUrl.length();
        assertThat(urlSize).isEqualTo(StringGenerator.urlStringSize);

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
    void getUniqueURLsTest() {
        List<UrlShortener> allURLs = UrlShortenerExample.getUrlShortenerExamples();
        List<UrlShortener> expectedURLs = UrlShortenerExample.getUniqueURLsExamples();

        List<UrlShortener> uniqueURLs = underTest.getUniqueURLs(allURLs);

        assertThat(uniqueURLs).usingRecursiveComparison().isEqualTo(expectedURLs);
    }

    @Test
    void getStatisticsTest() {
        String accountId = "karlo";

        List<UrlShortener> allURLs = UrlShortenerExample.getUrlShortenerExamples();
        List<UrlShortener> expectedURLs = UrlShortenerExample.getUniqueURLsExamples();

        given(repository.findByAccountId(accountId)).willReturn(allURLs);

        List<UrlShortener> resultURLs = underTest.getStatistics(accountId);

        assertThat(resultURLs).usingRecursiveComparison().isEqualTo(expectedURLs);
    }

    @Test
    void getAccountFromTokenTestFail() {
        String accountId = "karlo";
        String password = "password";

        String token = TokenEncoder.getBasicAuthorizationToken(accountId, password);

        given(accountRepository.findByAccountIdAndPassword(accountId, password)).willReturn(null);
        Account account = underTest.getAccountFromToken(token);

        assertThat(account).isNull();
    }

    @Test
    void getAccountFromTokenTestSuccess() {
        String accountId = "karlo";
        String password = "password";

        String token = TokenEncoder.getBasicAuthorizationToken(accountId, password);

        Account exists = new Account();
        exists.setAccountId(accountId);
        exists.setPassword(password);

        given(accountRepository.findByAccountIdAndPassword(accountId, password)).willReturn(exists);
        Account account = underTest.getAccountFromToken(token);

        assertThat(account).isNotNull();
        assertThat(account.getAccountId()).isEqualTo(accountId);
        assertThat(account.getPassword()).isEqualTo(password);
    }
}