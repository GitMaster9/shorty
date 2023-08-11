package com.example.restapi.service;

import com.example.core.model.UrlShortener;
import com.example.repository.UrlShortenerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class RedirectServiceTest {

    @Mock
    private UrlShortenerRepository urlShortenerRepository;
    private RedirectService underTest;

    @BeforeEach
    void setUp() {
        underTest = new RedirectService(urlShortenerRepository);
    }

    @Test
    void getUrlFailTest() {
        String shortUrl = "abcdefg";

        given(urlShortenerRepository.findByShortUrl(shortUrl)).willReturn(null);
        UrlShortener urlShortener = underTest.redirectUrl(shortUrl);

        assertThat(urlShortener).isNull();
    }

    @Test
    void getUrlSuccessTest() {
        String shortUrl = "abcdefg";
        String url = "https://minecraft.fandom.com/wiki/Minecraft_Wiki";
        int redirectType = 302;

        UrlShortener exists = new UrlShortener();
        exists.setUrl(url);
        exists.setRedirectType(redirectType);
        exists.setAccountId("karlo");

        given(urlShortenerRepository.findByShortUrl(shortUrl)).willReturn(exists);
        UrlShortener found = underTest.redirectUrl(shortUrl);

        assertThat(found).isNotNull();
        assertThat(found.getUrl()).isEqualTo(url);
        assertThat(found.getRedirectType()).isEqualTo(redirectType);
    }
}