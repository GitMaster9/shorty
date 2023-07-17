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
    private int clicks;

    public UrlShortener() {
    }

    public UrlShortener(Long id, String url, String shortUrl, int clicks) {
        this.id = id;
        this.url = url;
        this.shortUrl = shortUrl;
        this.clicks = clicks;
    }

    public UrlShortener(String url, String shortUrl, int clicks) {
        this.url = url;
        this.shortUrl = shortUrl;
        this.clicks = clicks;
    }

    public UrlShortener(String url) {
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }
}
