package com.example.shorty.frontend;

import com.example.shorty.account.Account;
import com.example.shorty.urlshortener.UrlShortener;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Controller
public class FrontendController {

    private final String urlApiBase = "http://localhost:8080/";
    @GetMapping("register")
    public String showRegisterPage(Account account, Model model) {
        return "register";
    }

    @GetMapping("login")
    public String showLoginPage(Account account, Model model) {
        return "login";
    }

    @GetMapping("short")
    public String showShortPage() {
        return "short";
    }

    @GetMapping("statistics")
    public String showStatisticsPage() {
        return "statistics";
    }

    @GetMapping("redirect")
    public String showRedirectPage(UrlShortener urlShortener) {
        return "redirect";
    }

    @PostMapping("register")
    public String registerUser(@ModelAttribute Account account, Model model) {
        String accountId = account.getAccountId();

        String urlApi = "administration/register";
        WebClient client = WebClient.create(urlApiBase);

        Map<String, String> data = new HashMap<>();
        data.put("accountId", accountId);

        WebClient.ResponseSpec responseSpec = client.post()
                .uri(urlApi)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(data), HashMap.class)
                .retrieve()
                .onStatus(
                        status -> status == HttpStatus.BAD_REQUEST,
                        clientResponse -> Mono.empty()
                );

        HashMap response = responseSpec.bodyToMono(HashMap.class).block();

        boolean success = (boolean) response.get("success");
        if (success) {
            String password = response.get("password").toString();
            model.addAttribute("successMessage", "Password: " + password);
        }
        else {
            String description = response.get("description").toString();
            model.addAttribute("errorMessage", "Description: " + description);
        }

        return "register";
    }

    @PostMapping("login")
    public String loginUser(@ModelAttribute Account account, Model model) {
        String accountId = account.getAccountId();
        String password = account.getPassword();

        String urlApi = "administration/login";
        WebClient client = WebClient.create(urlApiBase);

        Map<String, String> data = new HashMap<>();
        data.put("accountId", accountId);
        data.put("password", password);

        WebClient.ResponseSpec responseSpec = client.post()
                .uri(urlApi)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(data), HashMap.class)
                .retrieve()
                .onStatus(
                        status -> status == HttpStatus.BAD_REQUEST,
                        clientResponse -> Mono.empty()
                );

        HashMap response = responseSpec.bodyToMono(HashMap.class).block();

        boolean success = (boolean) response.get("success");
        if (success) {
            model.addAttribute("successMessage", "Login successful");
        }
        else {
            model.addAttribute("errorMessage", "Login failed");
        }

        return "login";
    }

    @GetMapping("geturl")
    public ResponseEntity<Void> redirectShortURLGet(@ModelAttribute UrlShortener urlShortener) {
        String shortUrl = urlShortener.getShortUrl();

        String urlApi = "redirection/get?shortUrl=" + shortUrl;
        WebClient client = WebClient.create(urlApiBase);

        WebClient.ResponseSpec responseSpec = client.get()
                .uri(urlApi)
                .retrieve()
                .onStatus(
                        status -> status == HttpStatus.NOT_FOUND,
                        clientResponse -> Mono.empty()
                );

        UrlShortener urlShortener1 = responseSpec.bodyToMono(UrlShortener.class).block();

        if (urlShortener1 == null) {
            return ResponseEntity.status(404).build();
        }

        String url = urlShortener1.getUrl();
        int redirectType = urlShortener1.getRedirectType();

        return ResponseEntity.status(redirectType).location(URI.create(url)).build();
    }
}
