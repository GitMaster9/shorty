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

    public ResponseEntity<Object> getShortURL(Map<String, Object> requestMap) {
        Object urlObject = requestMap.get("url");
        if (urlObject == null) {
            return createShortFailResponse("Failed - no 'url' field in request body");
        }

        String url = urlObject.toString();

        Optional<UrlShortener> urlShortenerOptional = urlShortenerRepository.findUrlShortenerByUrl(url);
        if (urlShortenerOptional.isPresent()) {
            String shortUrl = urlShortenerOptional.get().getShortUrl();
            return createShortSuccessResponse(shortUrl);
        }

        String shortUrl = generateShortUrl();
        UrlShortener urlShortener = new UrlShortener(url, shortUrl, 0);
        urlShortenerRepository.save(urlShortener);

        return createShortSuccessResponse(urlShortener.getShortUrl());
    }

    private String generateShortUrl() {
        String urlStart = "https://shorty.com/";

        String shortUrl = null;

        boolean urlExists = true;
        while (urlExists) {
            shortUrl = urlStart + getRandomString();
            urlExists = checkIfShortUrlExists(shortUrl);
        }

        return shortUrl;
    }

    private String getRandomString() {
        String alphanumericCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";
        int stringSize = 7;

        StringBuilder randomString = new StringBuilder(stringSize);
        Random random = new Random();

        for (int i = 0; i < stringSize; i++) {
            int randomIndex = random.nextInt(alphanumericCharacters.length());
            char randomChar = alphanumericCharacters.charAt(randomIndex);
            randomString.append(randomChar);
        }

        return randomString.toString();
    }

    private boolean checkIfShortUrlExists(String shortUrl) {
        Optional<UrlShortener> urlShortenerOptional = urlShortenerRepository.findUrlShortenerByShortUrl(shortUrl);

        return urlShortenerOptional.isPresent();
    }

    public ResponseEntity<Object> createShortSuccessResponse(String shortUrl) {
        Map<String, String> data = new HashMap<>();
        data.put("shortUrl", shortUrl);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    public ResponseEntity<Object> createShortFailResponse(String description) {
        Map<String, String> data = new HashMap<>();
        data.put("description", description);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
