package com.example.shorty.repository;

import com.example.shorty.restapi.UrlShortener;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UrlShortenerRepository extends JpaRepository<UrlShortener, String> {
    UrlShortener findByShortUrl(String shortUrl);

    List<UrlShortener> findByAccountId(String accountId);
}