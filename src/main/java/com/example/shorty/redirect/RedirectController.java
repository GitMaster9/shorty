package com.example.shorty.redirect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "redirection")
public class RedirectController {

    private final RedirectService redirectService;

    @Autowired
    public RedirectController(RedirectService redirectService) {
        this.redirectService = redirectService;
    }

    @GetMapping("get")
    public ResponseEntity<Object> redirectURL(@RequestParam String shortUrl) {
        return redirectService.getUrl(shortUrl);
    }
}
