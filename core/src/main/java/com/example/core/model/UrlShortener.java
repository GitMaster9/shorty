package com.example.core.model;

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

    public void addRedirects(int add) {
        redirects += add;
    }

    public void incrementRedirects() {
        redirects += 1;
    }
}
