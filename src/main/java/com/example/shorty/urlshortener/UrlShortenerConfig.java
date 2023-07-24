package com.example.shorty.urlshortener;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class UrlShortenerConfig {
    @Bean
    CommandLineRunner commandLineRunnerUrlShortener(UrlShortenerRepository repository) {
        return args -> {
            UrlShortener url1 = new UrlShortener("https://stackoverflow.com","https://shorty.com/dummyurl1", "johnny", 1);
            UrlShortener url2 = new UrlShortener("https://youtube.com","https://shorty.com/dummyurl2", "johnny", 0);
            UrlShortener url3 = new UrlShortener("https://youtube.com","https://shorty.com/dummyurl3", "johnny", 0);

            repository.saveAll(List.of(url1, url2, url3));
        };
    }
}
