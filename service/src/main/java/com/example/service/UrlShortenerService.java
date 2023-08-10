package com.example.service;

import com.example.core.model.Account;
import com.example.core.model.UrlShortener;
import com.example.repository.AccountRepository;
import com.example.repository.UrlShortenerRepository;
import com.example.utils.StringGenerator;
import com.example.utils.StringGeneratorType;
import com.example.utils.TokenEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
//@ComponentScan(basePackageClasses = {UrlShortenerRepository.class, AccountRepository.class})
public class UrlShortenerService {
    private final UrlShortenerRepository urlShortenerRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public UrlShortenerService(UrlShortenerRepository urlShortenerRepository, AccountRepository accountRepository) {
        this.urlShortenerRepository = urlShortenerRepository;
        this.accountRepository = accountRepository;
    }

    public String shortURL(Account account, String url, int redirectType) {
        final String shortUrl = generateShortUrl();

        final UrlShortener urlShortener = new UrlShortener();
        urlShortener.setUrl(url);
        urlShortener.setShortUrl(shortUrl);
        urlShortener.setAccountId(account.getAccountId());
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
        final UrlShortener urlShortener = urlShortenerRepository.findByShortUrl(shortUrl);

        return urlShortener != null;
    }

    public List<UrlShortener> getStatistics(String accountId) {
        final List<UrlShortener> allURLs = urlShortenerRepository.findByAccountId(accountId);

        return getUniqueURLs(allURLs);
    }

    public List<UrlShortener> getUniqueURLs(List<UrlShortener> allURLs) {
        final List<UrlShortener> uniqueURLs = new ArrayList<>();

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

    public Account getAuthenticatedAccount(String token) {
        final String[] decodedArray = TokenEncoder.decodeBasicToken(token);
        if (decodedArray == null || decodedArray.length != 2) {
            return null;
        }

        final Account account = new Account();
        account.setAccountId(decodedArray[0]);
        account.setPassword(decodedArray[1]);

        return accountRepository.findByAccountIdAndPassword(account.getAccountId(), account.getPassword());
    }
}
