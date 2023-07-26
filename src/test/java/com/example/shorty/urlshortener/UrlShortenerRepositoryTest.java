package com.example.shorty.urlshortener;

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
        UrlShortener newUrlShortener = new UrlShortener("www.youtube.com", shortUrl, "karlo", 302, 0);
        underTest.save(newUrlShortener);

        // when
        UrlShortener exists = underTest.findUrlShortenerByShortUrl(shortUrl);

        // then
        assertThat(exists).isEqualTo(newUrlShortener);
    }

    @Test
    void checkIfDoesNotFindUrlShortenerByShortUrl() {
        //given
        String shortUrl = "www.shorty.com";

        // when
        UrlShortener exists = underTest.findUrlShortenerByShortUrl(shortUrl);

        // then
        assertThat(exists).isNull();
    }

    @Test
    void findAllUrlShortenersByUser() {
        //given
        String accountId = "karlo";
        int expected = 3;

        UrlShortener url1 = new UrlShortener("www.youtube.com", "shorty1", accountId, 302, 0);
        UrlShortener url2 = new UrlShortener("www.facebook.com", "shorty2", accountId, 302, 1);
        UrlShortener url3 = new UrlShortener("www.instagram.com", "shorty3", "alex", 302, 2);
        UrlShortener url4 = new UrlShortener("www.stackoverflow.com", "shorty4", "johnny", 302, 3);
        UrlShortener url5 = new UrlShortener("www.vimeo.com", "shorty5", accountId, 302, 4);

        underTest.save(url1);
        underTest.save(url2);
        underTest.save(url3);
        underTest.save(url4);
        underTest.save(url5);

        // when
        List<UrlShortener> allUrls = underTest.findAllUrlShortenersByUser(accountId);
        int result = allUrls.size();

        // then
        assertThat(result).isEqualTo(expected);
    }
}