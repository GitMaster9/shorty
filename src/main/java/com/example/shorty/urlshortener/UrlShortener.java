package com.example.shorty.urlshortener;

import jakarta.persistence.*;

@Entity
@Table
public class UrlShortener {
    @Id
    @SequenceGenerator(
            name = "urlshortener_sequence",
            sequenceName = "urlshortener_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "urlshortener_sequence"
    )
    private Long id;
    private String url;
    private String shortUrl;
    private int redirects;

    public UrlShortener() {
    }

    public UrlShortener(String url, String shortUrl, int redirects) {
        this.url = url;
        this.shortUrl = shortUrl;
        this.redirects = redirects;
    }

    public String getUrl() {
        return url;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public int getRedirects() {
        return redirects;
    }

    public void incrementRedirects() {
        redirects += 1;
    }
}
