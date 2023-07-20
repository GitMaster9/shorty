package com.example.shorty.urlshortener;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UrlShortenerRepository extends JpaRepository<UrlShortener, String> {
    @Query("SELECT s FROM UrlShortener s WHERE s.shortUrl = ?1")
    Optional<UrlShortener> findUrlShortenerByShortUrl(String shortUrl);

    @Query("SELECT s FROM UrlShortener s WHERE s.accountId = ?1")
    List<UrlShortener> findAllUrlShortenersByUser(String accountId);
}