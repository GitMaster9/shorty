package com.example.shorty.redirect;

import com.example.shorty.urlshortener.UrlShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping(path = "redirection")
public class RedirectController {
    private final UrlShortenerService urlShortenerService;

    @Autowired
    public RedirectController(UrlShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    @PostMapping(path = "test")
    public void getShortURL(@RequestBody Map<String, Object> requestMap) {
        Object shortUrlObject = requestMap.get("shortUrl");
        if (shortUrlObject != null) {
            String shortUrl = shortUrlObject.toString();
            urlShortenerService.redirectUrl(shortUrl);
        }
    }
}
