package com.example.repository;

import com.example.core.model.UrlShortener;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UrlShortenerRepositoryTest {

    @Autowired
    private UrlShortenerRepository underTest;

    @Test
    void checkIfFindUrlShortenerByShortUrl() {
        //given
        String shortUrl = "www.shorty.com";

        UrlShortener newUrlShortener = new UrlShortener();
        newUrlShortener.setUrl("www.youtube.com");
        newUrlShortener.setShortUrl(shortUrl);
        newUrlShortener.setAccountId("karlo");
        newUrlShortener.setRedirectType(302);
        newUrlShortener.setRedirects(0);
        underTest.save(newUrlShortener);

        // when
        UrlShortener exists = underTest.findByShortUrl(shortUrl);

        // then
        assertThat(exists).isEqualTo(newUrlShortener);
    }

    @Test
    void checkIfDoesNotFindUrlShortenerByShortUrl() {
        //given
        String shortUrl = "www.shorty.com";

        // when
        UrlShortener exists = underTest.findByShortUrl(shortUrl);

        // then
        assertThat(exists).isNull();
    }

    @Test
    void findAllUrlShortenersByUser() {
        //given
        String accountId = "karlo";
        int expected = 3;

        UrlShortener url1 = new UrlShortener();
        url1.setUrl("www.youtube.com");
        url1.setShortUrl("shorty1");
        url1.setAccountId(accountId);
        url1.setRedirectType(302);
        url1.setRedirects(0);

        UrlShortener url2 = new UrlShortener();
        url2.setUrl("www.facebook.com");
        url2.setShortUrl("shorty2");
        url2.setAccountId(accountId);
        url2.setRedirectType(302);
        url2.setRedirects(1);

        UrlShortener url3 = new UrlShortener();
        url3.setUrl("www.instagram.com");
        url3.setShortUrl("shorty3");
        url3.setAccountId("alex");
        url3.setRedirectType(302);
        url3.setRedirects(2);

        UrlShortener url4 = new UrlShortener();
        url4.setUrl("www.stackoverflow.com");
        url4.setShortUrl("shorty4");
        url4.setAccountId("johnny");
        url4.setRedirectType(302);
        url4.setRedirects(3);

        UrlShortener url5 = new UrlShortener();
        url5.setUrl("www.vimeo.com");
        url5.setShortUrl("shorty5");
        url5.setAccountId(accountId);
        url5.setRedirectType(302);
        url5.setRedirects(4);

        // when
        underTest.save(url1);
        underTest.save(url2);
        underTest.save(url3);
        underTest.save(url4);
        underTest.save(url5);

        List<UrlShortener> allUrls = underTest.findByAccountId(accountId);
        int result = allUrls.size();

        // then
        assertThat(result).isEqualTo(expected);
    }
}