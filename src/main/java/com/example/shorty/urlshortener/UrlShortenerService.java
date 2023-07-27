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

    @Autowired
    public UrlShortenerService(UrlShortenerRepository urlShortenerRepository, AccountRepository accountRepository) {
        this.urlShortenerRepository = urlShortenerRepository;
        this.accountRepository = accountRepository;
    }

    public ResponseEntity<Object> getShortURL(String authorizationToken, Map<String, Object> requestMap) {
        Account account = getAccountFromToken(authorizationToken);
        if (account == null) {
            return createShortResponse(false, "Failed - Basic token is not valid");
        }

        Object urlObject = requestMap.get("url");
        if (urlObject == null) {
            return createShortResponse(false, "Failed - no 'url' field in request body");
        }

        int redirectType;
        Object redirectTypeObject = requestMap.get("redirectType");
        if (redirectTypeObject == null) {
            redirectType = 302;
        }
        else {
            redirectType = (int) redirectTypeObject;
        }

        String url = urlObject.toString();
        String shortUrl = generateShortUrl();
        String accountId = account.getAccountId();
        UrlShortener urlShortener = new UrlShortener(url, shortUrl, accountId, redirectType, 0);
        urlShortenerRepository.save(urlShortener);

        return createShortResponse(true, urlShortener.getShortUrl());
    }

    private String generateShortUrl() {
        String shortUrl = null;

        boolean urlExists = true;
        while (urlExists) {
            shortUrl = StringGenerator.generateUrl();
            urlExists = checkIfShortUrlExists(shortUrl);
        }

        return shortUrl;
    }

    private boolean checkIfShortUrlExists(String shortUrl) {
        UrlShortener urlShortener = urlShortenerRepository.findUrlShortenerByShortUrl(shortUrl);

        return urlShortener != null;
    }

    public ResponseEntity<Object> createShortResponse(boolean success, String helper) {
        Map<String, String> data = new HashMap<>();
        if (success) {
            data.put("shortUrl", helper);
        }
        else {
            data.put("description", helper);
        }
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    public ResponseEntity<Object> getStatistics(String authorizationToken) {
        Account account = getAccountFromToken(authorizationToken);
        if (account == null) {
            return createShortResponse(false, "Failed - Basic token is not valid");
        }

        List<UrlShortener> allURLs = urlShortenerRepository.findAllUrlShortenersByUser(account.getAccountId());

        List<UrlShortener> uniqueURLs = getUniqueURLs(allURLs);
        Map<String, Object> data = getUniqueURLsData(uniqueURLs);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    public List<UrlShortener> getUniqueURLs(List<UrlShortener> allURLs) {
        List<UrlShortener> uniqueURLs = new ArrayList<>();

        for (UrlShortener current : allURLs) {
            String currentUrl = current.getUrl();

            boolean foundUnique = false;
            for (UrlShortener unique : uniqueURLs) {
                String url = unique.getUrl();
                if (url.equals(currentUrl)) {
                    int newRedirects = current.getRedirects();
                    unique.addRedirects(newRedirects);
                    foundUnique = true;
                    break;
                }
            }

            if (foundUnique) continue;

            UrlShortener newUnique = new UrlShortener(currentUrl, current.getAccountId(), current.getRedirects());
            uniqueURLs.add(newUnique);
        }

        return uniqueURLs;
    }

    public Map<String, Object> getUniqueURLsData(List<UrlShortener> uniqueURLs) {
        Map<String, Object> data = new HashMap<>();

        for (UrlShortener unique : uniqueURLs) {
            String url = unique.getUrl();
            int redirects = unique.getRedirects();
            data.put(url, redirects);
        }

        return data;
    }

    public Account getAccountFromToken(String token) {
        if (!token.startsWith(TokenEncoder.basicTokenStart)) return null;

        String[] decodedStrings = TokenEncoder.decodeBasicToken(token);
        String accountId = decodedStrings[0];
        String password = decodedStrings[1];

        return accountRepository.findAccountByIdAndPassword(accountId, password);
    }
}
