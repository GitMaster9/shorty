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
    void getAuthenticatedAccountTestFail() {
        String token = "";

        Account found = underTest.getAuthenticatedAccount(token);
        assertThat(found).isNull();
    }

    @Test
    void getAuthenticatedAccountTestSuccess() {
        String accountId = "karlo";
        String password = "password";
        String token = TokenEncoder.getBasicAuthorizationToken(accountId, password);

        Account expected = new Account();
        expected.setAccountId(accountId);
        expected.setPassword(password);

        given(accountRepository.findByAccountIdAndPassword(accountId, password)).willReturn(expected);

        Account found = underTest.getAuthenticatedAccount(token);
        assertThat(found).usingRecursiveComparison().isEqualTo(expected);
    }
}