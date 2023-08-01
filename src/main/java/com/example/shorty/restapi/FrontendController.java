package com.example.shorty.restapi;

import com.example.shorty.utils.TokenEncoder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class FrontendController {

    @GetMapping(ControllerPath.REGISTER)
    public String showRegisterPage(Account account, Model model) {
        return "register";
    }

    @GetMapping(ControllerPath.LOGIN)
    public String showLoginPage(Account account, Model model) {
        return "login";
    }

    @GetMapping(ControllerPath.SHORT)
    public String showShortPage(ShortingRequest shortingRequest, Model model) {
        return "short";
    }

    @GetMapping(ControllerPath.STATISTICS)
    public String showStatisticsPage(Account account, Model model) {
        return "statistics";
    }

    @GetMapping(ControllerPath.REDIRECT)
    public String showRedirectPage(UrlShortener urlShortener) {
        return "redirect";
    }

    @PostMapping(ControllerPath.REGISTER)
    public String registerUser(@ModelAttribute Account account, Model model) {
        String accountId = account.getAccountId();

        WebClient client = WebClient.create(ControllerPath.API_URL_BASE);

        Map<String, String> data = new HashMap<>();
        data.put("accountId", accountId);

        WebClient.ResponseSpec responseSpec = client.post()
                .uri(ControllerPath.ADMINISTRATION_REGISTER)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(data), HashMap.class)
                .retrieve()
                .onStatus(
                        status -> status == HttpStatus.BAD_REQUEST,
                        clientResponse -> Mono.empty()
                );

        HashMap response = responseSpec.bodyToMono(HashMap.class).block();

        if (response == null) {
            model.addAttribute("errorMessage", "Error: response object is null");
            return "short";
        }

        boolean success = (boolean) response.get("success");
        if (success) {
            String password = response.get("password").toString();
            model.addAttribute("successMessage", "Password: " + password);
        }
        else {
            String description = response.get("description").toString();
            model.addAttribute("errorMessage", description);
        }

        return "register";
    }

    @PostMapping(ControllerPath.LOGIN)
    public String loginUser(@ModelAttribute Account account, Model model) {
        String accountId = account.getAccountId();
        String password = account.getPassword();

        WebClient client = WebClient.create(ControllerPath.API_URL_BASE);

        Map<String, String> data = new HashMap<>();
        data.put("accountId", accountId);
        data.put("password", password);

        WebClient.ResponseSpec responseSpec = client.post()
                .uri(ControllerPath.ADMINISTRATION_LOGIN)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(data), HashMap.class)
                .retrieve()
                .onStatus(
                        status -> status == HttpStatus.BAD_REQUEST,
                        clientResponse -> Mono.empty()
                );

        HashMap response = responseSpec.bodyToMono(HashMap.class).block();

        if (response == null) {
            model.addAttribute("errorMessage", "Error: response object is null");
            return "short";
        }

        boolean success = (boolean) response.get("success");
        if (success) {
            model.addAttribute("successMessage", "Login successful");
        }
        else {
            model.addAttribute("errorMessage", "Login failed");
        }

        return "login";
    }

    @PostMapping(ControllerPath.SHORT)
    public String shortUrl(ShortingRequest shortingRequest, Model model) {
        String accountId = shortingRequest.getAccountId();
        String password = shortingRequest.getPassword();
        String url = shortingRequest.getUrl();
        String redirectTypeString = shortingRequest.getRedirectType();

        int redirectType;
        try {
            redirectType = Integer.parseInt(redirectTypeString);
        }
        catch (NumberFormatException nfe) {
            redirectType = 400;
        }

        if (redirectType != 301 && redirectType != 302) {
            model.addAttribute("errorMessage", "Error: redirect type can be only be 301 (Moved Permanently) or 302 (Found)");
            return "short";
        }

        String token = TokenEncoder.encodeCredentials(accountId, password);

        Map<String, Object> data = new HashMap<>();
        data.put("url", url);
        data.put("redirectType", redirectType);

        WebClient client = WebClient.create(ControllerPath.API_URL_BASE);

        WebClient.ResponseSpec responseSpec = client.post()
                .uri(ControllerPath.ADMINISTRATION_SHORT)
                .headers(httpHeaders -> httpHeaders.setBasicAuth(token))
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(data), HashMap.class)
                .retrieve()
                .onStatus(
                        status -> status == HttpStatus.BAD_REQUEST,
                        clientResponse -> Mono.empty()
                );

        HashMap response = responseSpec.bodyToMono(HashMap.class).block();

        if (response == null) {
            model.addAttribute("errorMessage", "Error: response object is null");
            return "short";
        }

        Object descriptionObject = response.get("description");
        if (descriptionObject != null) {
            model.addAttribute("errorMessage", descriptionObject.toString());
            return "short";
        }

        Object shortUrlObject = response.get("shortUrl");
        if (shortUrlObject == null) {
            model.addAttribute("errorMessage", "Error: response invalid");
            return "short";
        }

        model.addAttribute("successMessage", "short URL: " + shortUrlObject);

        return "short";
    }

    @PostMapping(ControllerPath.STATISTICS)
    public String getStatistics(Account account, Model model) {
        String accountId = account.getAccountId();
        String password = account.getPassword();
        String token = TokenEncoder.encodeCredentials(accountId, password);

        WebClient client = WebClient.create(ControllerPath.API_URL_BASE);

        WebClient.ResponseSpec responseSpec = client.get()
                .uri(ControllerPath.ADMINISTRATION_STATISTICS)
                .headers(httpHeaders -> httpHeaders.setBasicAuth(token))
                .retrieve()
                .onStatus(
                        status -> status == HttpStatus.BAD_REQUEST,
                        clientResponse -> Mono.empty()
                );

        Map response = responseSpec.bodyToMono(Map.class).block();

        if (response == null) {
            model.addAttribute("errorMessage", "Error: response object is null");
            return "statistics";
        }

        List<UrlShortener> receivedURLs = new ArrayList<>();

        for (Object urlObject : response.keySet()) {
            String url = urlObject.toString();
            int redirects = (int) response.get(url);
            UrlShortener newUrl = new UrlShortener(url, redirects);
            receivedURLs.add(newUrl);
        }

        if (receivedURLs.isEmpty()) {
            model.addAttribute("successMessage", "This user has no registered URLs");
        }

        model.addAttribute("URLs", receivedURLs);

        return "statistics";
    }

    @GetMapping(ControllerPath.GET_URL)
    public ResponseEntity<Void> redirectShortURLGet(@ModelAttribute UrlShortener urlShortener) {
        String shortUrl = urlShortener.getShortUrl();
        System.out.println(shortUrl);

        String urlApi = ControllerPath.REDIRECTION_SHORT_URL + shortUrl;
        WebClient client = WebClient.create(ControllerPath.API_URL_BASE);

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
