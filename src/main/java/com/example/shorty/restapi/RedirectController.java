package com.example.shorty.restapi;

import com.example.shorty.service.RedirectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = ControllerPath.REDIRECTION)
public class RedirectController {

    private final RedirectService redirectService;

    @Autowired
    public RedirectController(RedirectService redirectService) {
        this.redirectService = redirectService;
    }

    @GetMapping()
    public ResponseEntity<Object> redirectURL(@RequestParam String shortUrl) {
        return redirectService.getUrl(shortUrl);
    }
}
