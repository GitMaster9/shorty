package com.example.shorty.service;

import com.example.shorty.restapi.UrlShortener;
import com.example.shorty.repository.UrlShortenerRepository;
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
        UrlShortener urlShortener = urlShortenerRepository.findByShortUrl(shortUrl);
        if (urlShortener == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        urlShortener.incrementRedirects();
        urlShortenerRepository.save(urlShortener);

        Map<String, Object> data = new HashMap<>();
        data.put("url", urlShortener.getUrl());
        data.put("redirectType", urlShortener.getRedirectType());
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
