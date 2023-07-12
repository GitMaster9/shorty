package com.example.shorty.urlshortener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "administration")
public class UrlShortenerController {
    private final UrlShortenerService urlShortenerService;

    @Autowired
    public UrlShortenerController(UrlShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    @PostMapping(path = "short")
    public ResponseEntity<Object> registerNewAccount(@RequestBody UrlShortener urlShortener) {
        return urlShortenerService.getShortUrl(urlShortener);
    }
}
