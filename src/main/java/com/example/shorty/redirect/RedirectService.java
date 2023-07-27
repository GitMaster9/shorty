package com.example.shorty.redirect;

import com.example.shorty.urlshortener.UrlShortener;
import com.example.shorty.urlshortener.UrlShortenerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class RedirectService {
    private final UrlShortenerRepository urlShortenerRepository;

    @Autowired
    public RedirectService(UrlShortenerRepository urlShortenerRepository) {
        this.urlShortenerRepository = urlShortenerRepository;
    }

    public ResponseEntity<Object> getUrl(String shortUrl) {
        System.out.println("RedirectService.getUrl()");
        UrlShortener urlShortener = urlShortenerRepository.findUrlShortenerByShortUrl(shortUrl);
        if (urlShortener == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        System.out.println("INKREMENTIRANJE");
        urlShortener.incrementRedirects();
        urlShortenerRepository.save(urlShortener);

        Map<String, Object> data = new HashMap<>();
        data.put("url", urlShortener.getUrl());
        data.put("redirectType", urlShortener.getRedirectType());
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
