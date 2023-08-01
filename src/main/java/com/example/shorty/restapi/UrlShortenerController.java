package com.example.shorty.restapi;

import com.example.shorty.service.UrlShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = ControllerPath.ADMINISTRATION)
public class UrlShortenerController {
    private final UrlShortenerService urlShortenerService;

    @Autowired
    public UrlShortenerController(UrlShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    @PostMapping(path = ControllerPath.SHORT)
    public ResponseEntity<Object> getShortURL(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationToken, @RequestBody Map<String, Object> requestMap) {
        String shortUrl = urlShortenerService.getShortURL(authorizationToken, requestMap);

        Map<String, Object> data = new HashMap<>();

        if (shortUrl != null) {
            data.put("shortUrl", shortUrl);
        }
        else {
            data.put("description", "ERROR: shortUrl == null");
        }

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping(path = ControllerPath.STATISTICS)
    public ResponseEntity<Object> getUserStatistics(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationToken) {
        return urlShortenerService.getStatistics(authorizationToken);
    }
}
