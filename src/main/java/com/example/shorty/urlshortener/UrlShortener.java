package com.example.shorty.urlshortener;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table
@NoArgsConstructor
@ToString
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
    @Getter
    private String url;
    @Getter
    @Setter
    private String shortUrl;
    @Getter
    private String accountId;
    @Getter
    private int redirectType;
    @Getter
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
