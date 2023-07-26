package com.example.shorty.frontend;

import com.example.shorty.urlshortener.UrlShortener;
import com.example.shorty.urlshortener.UrlShortenerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

@Controller
public class FrontendController {

    @Autowired
    private UrlShortenerRepository repository;

    @GetMapping("redirect")
    public String sayHello(Model model, UrlShortener urlShortener) {
        model.addAttribute("something", "test attribute");

        return "redirect";
    }

    @PostMapping("/redirect")
    public ResponseEntity<Void> redirectShortURL(@ModelAttribute("urlShortener") UrlShortener urlShortener) {
        String shortUrl = urlShortener.getShortUrl();

        UrlShortener found = repository.findUrlShortenerByShortUrl(shortUrl);
        if (found == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        String url = found.getUrl();
        int redirectType = found.getRedirectType();

        return ResponseEntity.status(redirectType).location(URI.create(url)).build();
    }
}
