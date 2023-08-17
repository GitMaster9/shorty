package com.example.restapi.controller;

import com.example.core.ControllerPath;
import com.example.core.model.Account;
import com.example.core.model.UrlShortener;
import com.example.restapi.exception.ApiBadRequestException;
import com.example.restapi.exception.ApiUnauthorizedException;
import com.example.restapi.exception.ExceptionMessages;
import com.example.restapi.service.UrlShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@RestController
@RequestMapping(path = ControllerPath.ADMINISTRATION)
public class UrlShortenerController {
    private final UrlShortenerService urlShortenerService;
    public final Logger logger = LogManager.getLogger(this);

    @Autowired
    public UrlShortenerController(UrlShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    @PostMapping(path = ControllerPath.SHORT)
    public ResponseEntity<Map<String, Object>> shortURL(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationToken, @RequestBody Map<String, Object> requestMap) {
        final String accountId = urlShortenerService.getAccountIdFromBearerToken(authorizationToken);

        final Account account = urlShortenerService.getAccountByAccountId(accountId);
        if (account == null) {
            logger.info("Unauthorized - " + ExceptionMessages.UNAUTHORIZED);
            throw new ApiUnauthorizedException(ExceptionMessages.UNAUTHORIZED);
        }

        final Object urlObject = requestMap.get("url");
        if (urlObject == null) {
            logger.info("Bad Request - " + ExceptionMessages.MISSING_URL);
            throw new ApiBadRequestException(ExceptionMessages.MISSING_URL);
        }

        final String url = urlObject.toString();

        int redirectType;
        final Object redirectTypeObject = requestMap.get("redirectType");
        if (redirectTypeObject == null) {
            redirectType = 302;
        }
        else {
            redirectType = (int) redirectTypeObject;
        }

        final String shortUrl = urlShortenerService.shortURL(account, url, redirectType);

        final Map<String, Object> data = new HashMap<>();
        data.put("shortUrl", shortUrl);

        return ResponseEntity.ok(data);
    }

    @GetMapping(path = ControllerPath.STATISTICS)
    public ResponseEntity<Map<String, Object>> getStatistics(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationToken) {
        final String accountId = urlShortenerService.getAccountIdFromBearerToken(authorizationToken);

        final Account account = urlShortenerService.getAccountByAccountId(accountId);
        if (account == null) {
            logger.info("Unauthorized - " + ExceptionMessages.UNAUTHORIZED);
            throw new ApiUnauthorizedException(ExceptionMessages.UNAUTHORIZED);
        }

        final List<UrlShortener> uniqueURLs = urlShortenerService.getStatistics(account.getAccountId());

        final Map<String, Object> data = new HashMap<>();

        for (UrlShortener unique : uniqueURLs) {
            String url = unique.getUrl();
            int redirects = unique.getRedirects();
            data.put(url, redirects);
        }

        return ResponseEntity.ok(data);
    }
}
