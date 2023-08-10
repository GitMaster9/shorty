package com.example.service;

import com.example.core.model.UrlShortener;
import com.example.repository.UrlShortenerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
//@ComponentScan(basePackageClasses = UrlShortenerRepository.class)
public class RedirectService {
    private final UrlShortenerRepository urlShortenerRepository;

    @Autowired
    public RedirectService(UrlShortenerRepository urlShortenerRepository) {
        this.urlShortenerRepository = urlShortenerRepository;
    }

    public UrlShortener redirectUrl(String shortUrl) {
        final UrlShortener urlShortener = urlShortenerRepository.findByShortUrl(shortUrl);
        if (urlShortener == null) {
            return null;
        }

        urlShortener.incrementRedirects();
        urlShortenerRepository.save(urlShortener);

        return urlShortener;
    }
}
