package com.example.shorty.restapi;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@NoArgsConstructor
@Getter
@Setter
public class UrlShortener {
    @Id
    @SequenceGenerator(
            name = TableConstant.UrlShortenerSequence,
            sequenceName = TableConstant.UrlShortenerSequence,
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = TableConstant.UrlShortenerSequence
    )
    private Long id;
    private String url;
    private String shortUrl;
    private String accountId;
    private int redirectType;
    private int redirects;

    public UrlShortener(String url, String shortUrl, String accountId, int redirectType, int redirects) {
        this.url = url;
        this.shortUrl = shortUrl;
        this.accountId = accountId;
        this.redirectType = redirectType;
        this.redirects = redirects;
    }

    public UrlShortener(String url, String accountId, int redirects) {
        this.url = url;
        this.accountId = accountId;
        this.redirects = redirects;
    }

    public UrlShortener(String url, int redirectType, String accountId) {
        this.url = url;
        this.redirectType = redirectType;
        this.accountId = accountId;
    }

    public UrlShortener(String url, int redirects) {
        this.url = url;
        this.redirects = redirects;
    }

    public void addRedirects(int add) {
        redirects += add;
    }

    public void incrementRedirects() {
        redirects += 1;
    }
}
