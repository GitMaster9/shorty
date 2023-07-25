package com.example.shorty.urlshortener;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    private String shortUrl;
    @Getter
    private String accountId;
    @Getter
    private int redirects;

    public UrlShortener(String url, String shortUrl, String accountId, int redirects) {
        this.url = url;
        this.shortUrl = shortUrl;
        this.accountId = accountId;
        this.redirects = redirects;
    }

    public UrlShortener(String url, String accountId, int redirects) {
        this.url = url;
        this.accountId = accountId;
        this.redirects = redirects;
    }

    public void incrementRedirects(int add) {
        redirects += add;
    }
}
