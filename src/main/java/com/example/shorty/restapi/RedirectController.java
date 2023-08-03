package com.example.shorty.restapi;

import com.example.shorty.exception.ApiNotFoundException;
import com.example.shorty.service.RedirectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = ControllerPath.REDIRECTION)
public class RedirectController {

    private final RedirectService redirectService;

    @Autowired
    public RedirectController(RedirectService redirectService) {
        this.redirectService = redirectService;
    }

    @GetMapping()
    public ResponseEntity<Map<String, Object>> redirectURL(@RequestParam String shortUrl) {
        final UrlShortener urlShortener = redirectService.redirectUrl(shortUrl);
        if (urlShortener == null) {
            throw new ApiNotFoundException("No URL matches the short URL given");
        }

        final Map<String, Object> data = new HashMap<>();
        data.put("url", urlShortener.getUrl());
        data.put("redirectType", urlShortener.getRedirectType());

        return ResponseEntity.ok(data);
    }
}
