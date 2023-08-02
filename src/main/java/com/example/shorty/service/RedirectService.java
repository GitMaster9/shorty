package com.example.shorty.service;

import com.example.shorty.restapi.UrlShortener;
import com.example.shorty.repository.UrlShortenerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedirectService {
    private final UrlShortenerRepository urlShortenerRepository;

    @Autowired
    public RedirectService(UrlShortenerRepository urlShortenerRepository) {
        this.urlShortenerRepository = urlShortenerRepository;
    }

    public UrlShortener redirectUrl(String shortUrl) {
        UrlShortener urlShortener = urlShortenerRepository.findByShortUrl(shortUrl);
        if (urlShortener == null) {
            return null;
        }

        urlShortener.incrementRedirects();
        urlShortenerRepository.save(urlShortener);

        return urlShortener;
    }
}
