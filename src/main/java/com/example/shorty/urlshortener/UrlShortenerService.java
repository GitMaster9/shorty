package com.example.shorty.urlshortener;

import com.example.shorty.account.Account;
import com.example.shorty.account.AccountRepository;
import com.example.shorty.generator.StringGenerator;
import com.example.shorty.token.TokenEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class UrlShortenerService {
    private final UrlShortenerRepository urlShortenerRepository;
    private final AccountRepository accountRepository;
    private final String basicTokenStart = "Basic ";

    @Autowired
    public UrlShortenerService(UrlShortenerRepository urlShortenerRepository, AccountRepository accountRepository) {
        this.urlShortenerRepository = urlShortenerRepository;
        this.accountRepository = accountRepository;
    }

    public ResponseEntity<Object> getShortURL(String authorizationToken, Map<String, Object> requestMap) {
        Account account = getAccountFromToken(authorizationToken);
        if (account == null) return createShortFailResponse("Failed - Basic token is not valid");

        Object urlObject = requestMap.get("url");
        if (urlObject == null) {
            return createShortFailResponse("Failed - no 'url' field in request body");
        }

        String url = urlObject.toString();

        String shortUrl = generateShortUrl();
        UrlShortener urlShortener = new UrlShortener(url, shortUrl, account.getAccountId(), 0);
        urlShortenerRepository.save(urlShortener);

        return createShortSuccessResponse(urlShortener.getShortUrl());
    }

    private String generateShortUrl() {
        String urlStart = "https://shorty.com/";

        String shortUrl = null;

        boolean urlExists = true;
        while (urlExists) {
            shortUrl = StringGenerator.generateUrl(urlStart);
            urlExists = checkIfShortUrlExists(shortUrl);
        }

        return shortUrl;
    }

    private boolean checkIfShortUrlExists(String shortUrl) {
        UrlShortener urlShortener = urlShortenerRepository.findUrlShortenerByShortUrl(shortUrl);

        return urlShortener != null;
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

    public ResponseEntity<Object> getStatistics(String authorizationToken) {
        Account account = getAccountFromToken(authorizationToken);
        if (account == null) return createShortFailResponse("Failed - Basic token is not valid");

        Map<String, Object> data = new HashMap<>();

        List<UrlShortener> allURLs = urlShortenerRepository.findAllUrlShortenersByUser(account.getAccountId());

        List<UrlShortener> uniqueURLs = new ArrayList<>();

        for (UrlShortener current : allURLs) {
            String currentUrl = current.getUrl();

            boolean foundUnique = false;
            for (UrlShortener unique : uniqueURLs) {
                String url = unique.getUrl();
                if (url.equals(currentUrl)) {
                    int newRedirects = current.getRedirects();
                    unique.incrementRedirects(newRedirects);
                    foundUnique = true;
                    break;
                }
            }

            if (foundUnique) continue;

            uniqueURLs.add(current);
        }

        for (UrlShortener unique : uniqueURLs) {
            String url = unique.getUrl();
            int redirects = unique.getRedirects();
            data.put(url, redirects);
        }

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    public Account getAccountFromToken(String token) {
        if (!token.startsWith(basicTokenStart)) return null;

        String[] decodedStrings = TokenEncoder.decodeBasicToken(token);
        String accountId = decodedStrings[0];
        String password = decodedStrings[1];

        return accountRepository.findAccountByIdAndPassword(accountId, password);
    }

    public void redirectUrl(String shortUrl) {
        UrlShortener urlShortener = urlShortenerRepository.findUrlShortenerByShortUrl(shortUrl);
        if (urlShortener != null) {
            urlShortener.incrementRedirects(1);
            urlShortenerRepository.save(urlShortener);
        }
    }
}
