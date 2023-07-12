package com.example.shorty.urlshortener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UrlShortenerService {
    private final UrlShortenerRepository urlShortenerRepository;

    @Autowired
    public UrlShortenerService(UrlShortenerRepository urlShortenerRepository) {
        this.urlShortenerRepository = urlShortenerRepository;
    }

    public ResponseEntity<Object> getShortUrl(UrlShortener urlShortener) {
        String fullUrl = urlShortener.getUrl();

        Optional<UrlShortener> urlShortenerOptional = urlShortenerRepository.findUrlShortenerByUrl(fullUrl);
        if (urlShortenerOptional.isPresent()) {
            String shortUrl = urlShortenerOptional.get().getShortUrl();
            urlShortener.setShortUrl(shortUrl);
        }
        else {
            String shortUrl = generateShortUrl();
            urlShortener.setShortUrl(shortUrl);

            urlShortenerRepository.save(urlShortener);
        }

        return createShortSuccessResponse(urlShortener.getShortUrl());
    }

    private String generateShortUrl() {
        String urlStart = "https://shorty.com/";

        List<UrlShortener> urlShorteners = urlShortenerRepository.findAll();
        String shortUrl = null;

        boolean urlExists = true;
        while (urlExists) {
            shortUrl = urlStart + getRandomString(7);
            urlExists = checkIfShortUrlExists(shortUrl, urlShorteners);
        }

        return shortUrl;
    }

    private String getRandomString(int length) {
        String alphanumericCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";

        StringBuilder randomString = new StringBuilder(length);
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(alphanumericCharacters.length());
            char randomChar = alphanumericCharacters.charAt(randomIndex);
            randomString.append(randomChar);
        }

        return randomString.toString();
    }

    private boolean checkIfShortUrlExists(String shortUrl, List<UrlShortener> urlShorteners) {
        boolean exists = false;

        Optional<UrlShortener> urlShortenerOptional = urlShortenerRepository.findUrlShortenerByShortUrl(shortUrl);
        if (urlShortenerOptional.isPresent()) {
            exists = true;
        }

        return exists;
    }

    public ResponseEntity<Object> createShortSuccessResponse(String shortUrl) {
        Map<String, String> data = new HashMap<>();
        data.put("shortUrl", shortUrl);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
