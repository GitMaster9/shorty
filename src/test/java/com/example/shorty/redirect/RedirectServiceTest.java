package com.example.shorty.redirect;

import com.example.shorty.responsehandler.ResponseHandler;
import com.example.shorty.urlshortener.UrlShortener;
import com.example.shorty.urlshortener.UrlShortenerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

        ResponseEntity<Object> responseEntity = underTest.getUrl(shortUrl);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getUrlSuccessTest() {
        String shortUrl = "abcdefg";
        String url = "https://minecraft.fandom.com/wiki/Minecraft_Wiki";
        int redirectType = 302;

        given(urlShortenerRepository.findUrlShortenerByShortUrl(shortUrl)).willReturn(new UrlShortener(url, redirectType, "karlo"));
        ResponseEntity<Object> responseEntity = underTest.getUrl(shortUrl);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        Object urlObject = ResponseHandler.getDataFieldFromResponse(responseEntity, "url");
        String urlReceived = urlObject.toString();
        assertThat(urlReceived).isEqualTo(url);

        Object redirectTypeObject = ResponseHandler.getDataFieldFromResponse(responseEntity, "redirectType");
        int redirectTypeReceived = (int) redirectTypeObject;
        assertThat(redirectTypeReceived).isEqualTo(redirectType);
    }
}