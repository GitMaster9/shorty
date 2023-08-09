package com.example.shorty.restapi;

import com.example.shorty.exception.ApiNotFoundException;
import com.example.shorty.exception.ExceptionMessages;
import com.example.shorty.service.RedirectService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = ControllerPath.REDIRECTION)
public class RedirectController {
    private final RedirectService redirectService;
    public static final Logger logger = LogManager.getLogger(RedirectController.class);

    @Autowired
    public RedirectController(RedirectService redirectService) {
        this.redirectService = redirectService;
    }

    @GetMapping()
    public ResponseEntity<Map<String, Object>> redirectURL(@RequestParam String shortUrl) {
        final UrlShortener urlShortener = redirectService.redirectUrl(shortUrl);
        if (urlShortener == null) {
            logger.info("Not Found - " + ExceptionMessages.URL_NOT_FOUND);
            throw new ApiNotFoundException(ExceptionMessages.URL_NOT_FOUND);
        }

        final Map<String, Object> data = new HashMap<>();
        data.put("url", urlShortener.getUrl());
        data.put("redirectType", urlShortener.getRedirectType());

        return ResponseEntity.ok(data);
    }
}
