package com.example.repository;

import com.example.core.model.UrlShortener;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UrlShortenerRepository extends JpaRepository<UrlShortener, String> {
    UrlShortener findByShortUrl(String shortUrl);

    List<UrlShortener> findByAccountId(String accountId);
}