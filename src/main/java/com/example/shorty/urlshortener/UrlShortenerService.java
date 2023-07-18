package com.example.shorty.urlshortener;

import com.example.shorty.account.Account;
import com.example.shorty.account.AccountRepository;
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

        Optional<UrlShortener> urlShortenerOptional = urlShortenerRepository.findURL(account.getAccountId(), url);
        if (urlShortenerOptional.isPresent()) {
            String shortUrl = urlShortenerOptional.get().getShortUrl();
            return createShortSuccessResponse(shortUrl);
        }

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

    public ResponseEntity<Object> getStatistics(String authorizationToken) {
        Account account = getAccountFromToken(authorizationToken);
        if (account == null) return createShortFailResponse("Failed - Basic token is not valid");

        Map<String, Object> data = new HashMap<>();

        List<UrlShortener> allURLs = urlShortenerRepository.findAllUrlShortenersByUser(account.getAccountId());

        for (UrlShortener current : allURLs) {
            String url = current.getUrl();
            int redirects = current.getRedirects();
            data.put(url, redirects);
        }

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    private Account getAccountFromToken(String token) {
        if (!token.startsWith(basicTokenStart)) return null;

        String[] decodedStrings = decodeBasicToken(token);
        String accountId = decodedStrings[0];
        String password = decodedStrings[1];

        if (!authenticateAccount(accountId, password)) {
            return null;
        }

        return new Account(accountId, password);
    }

    private String[] decodeBasicToken(String token) {
        String encodedString = token.replaceFirst(basicTokenStart, "");

        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        String decodedString = new String(decodedBytes);

        return decodedString.split(":", 2);
    }

    private boolean authenticateAccount(String accountId, String password) {
        Optional<Account> accountOptional = accountRepository.findAccountByAccountId(accountId);
        return accountOptional.filter(account -> password.equals(account.getPassword())).isPresent();
    }

    public void redirectUrl(String shortUrl) {
        Optional<UrlShortener> urlShortenerOptional = urlShortenerRepository.findUrlShortenerByShortUrl(shortUrl);
        if (urlShortenerOptional.isPresent()) {
            UrlShortener urlShortener = urlShortenerOptional.get();
            urlShortener.incrementRedirects();
            urlShortenerRepository.save(urlShortener);
        }
    }
}
