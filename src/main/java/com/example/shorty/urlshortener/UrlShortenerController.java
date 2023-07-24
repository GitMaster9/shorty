package com.example.shorty.urlshortener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping(path = "administration")
public class UrlShortenerController {
    private final UrlShortenerService urlShortenerService;

    @Autowired
    public UrlShortenerController(UrlShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    @PostMapping(path = "short")
    public ResponseEntity<Object> getShortURL(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationToken, @RequestBody Map<String, Object> requestMap) {
        return urlShortenerService.getShortURL(authorizationToken, requestMap);
    }

    @GetMapping(path = "statistics")
    public ResponseEntity<Object> getUserStatistics(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationToken) {
        return urlShortenerService.getStatistics(authorizationToken);
    }
}
