package com.example.shorty.service;

import com.example.shorty.repository.UrlShortenerRepository;
import com.example.shorty.restapi.Account;
import com.example.shorty.repository.AccountRepository;
import com.example.shorty.restapi.UrlShortener;
import com.example.shorty.utils.StringGenerator;
import com.example.shorty.utils.StringGeneratorType;
import com.example.shorty.utils.TokenEncoder;
import org.springframework.beans.factory.annotation.Autowired;
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

    public String shortURL(Account account, String url, int redirectType) {
        String shortUrl = generateShortUrl();
        String accountId = account.getAccountId();

        UrlShortener urlShortener = new UrlShortener();
        urlShortener.setUrl(url);
        urlShortener.setShortUrl(shortUrl);
        urlShortener.setAccountId(accountId);
        urlShortener.setRedirectType(redirectType);
        urlShortener.setRedirects(0);
        urlShortenerRepository.save(urlShortener);

        return shortUrl;
    }

    private String generateShortUrl() {
        String shortUrl = null;

        boolean urlExists = true;
        while (urlExists) {
            shortUrl = StringGenerator.generateRandomString(StringGeneratorType.URL);
            urlExists = shortUrlExists(shortUrl);
        }

        return shortUrl;
    }

    private boolean shortUrlExists(String shortUrl) {
        UrlShortener urlShortener = urlShortenerRepository.findByShortUrl(shortUrl);

        return urlShortener != null;
    }

    public List<UrlShortener> getStatistics(String accountId) {
        List<UrlShortener> allURLs = urlShortenerRepository.findByAccountId(accountId);

        return getUniqueURLs(allURLs);
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

            UrlShortener newUnique = new UrlShortener();
            newUnique.setUrl(currentUrl);
            newUnique.setAccountId(current.getAccountId());
            newUnique.setRedirects(current.getRedirects());
            uniqueURLs.add(newUnique);
        }

        return uniqueURLs;
    }

    public Account getAccountFromToken(String token) {
        if (!token.startsWith(TokenEncoder.BASIC_TOKEN_START)) return null;

        String[] decodedStrings = TokenEncoder.decodeBasicToken(token);
        String accountId = decodedStrings[0];
        String password = decodedStrings[1];

        return accountRepository.findByAccountIdAndPassword(accountId, password);
    }
}
