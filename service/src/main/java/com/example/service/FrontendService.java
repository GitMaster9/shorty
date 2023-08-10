package com.example.service;

import com.example.core.model.Account;
import com.example.core.model.ShortingResponse;
import com.example.core.model.UrlShortener;
import com.example.core.ControllerPath;
import com.example.utils.TokenEncoder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FrontendService {

    final WebClient client = WebClient.create(ControllerPath.API_URL_BASE);

    public Account sendRegisterRequest(String accountId) {
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

        final HashMap response = responseSpec.bodyToMono(HashMap.class).block();

        if (response == null) {
            return null;
        }

        final boolean success = (boolean) response.get("success");

        if (!success) {
            return null;
        }

        final String password = response.get("password").toString();
        final Account account = new Account();
        account.setAccountId(accountId);
        account.setPassword(password);
        return account;
    }
    public boolean sendLoginRequest(String accountId, String password) {
        final Map<String, String> data = new HashMap<>();
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

        final HashMap response = responseSpec.bodyToMono(HashMap.class).block();

        if (response == null) {
            return false;
        }

        return (boolean) response.get("success");
    }

    public ShortingResponse sendShortRequest(String accountId, String password, String url, int redirectType) {
        final String token = TokenEncoder.encodeCredentials(accountId, password);

        final Map<String, Object> data = new HashMap<>();
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
                        status -> status == HttpStatus.UNAUTHORIZED,
                        clientResponse -> Mono.empty()
                );

        final ResponseEntity<String> responseEntity = responseSpec.toEntity(String.class).block();

        final ShortingResponse shortingResponse = new ShortingResponse();

        if (responseEntity == null || responseEntity.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            shortingResponse.setDescription("User credentials not valid");
            return shortingResponse;
        }

        HashMap response = responseSpec.bodyToMono(HashMap.class).block();

        if (response == null) {
            shortingResponse.setDescription("Api response is null");
            return shortingResponse;
        }

        Object descriptionObject = response.get("description");
        if (descriptionObject != null) {
            shortingResponse.setDescription(descriptionObject.toString());
            return shortingResponse;
        }

        Object shortUrlObject = response.get("shortUrl");
        if (shortUrlObject == null) {
            shortingResponse.setDescription("ERROR: no short url");
            return shortingResponse;
        }

        shortingResponse.setShortUrl(shortUrlObject.toString());
        shortingResponse.setDescription(null);

        return  shortingResponse;
    }

    public List<UrlShortener> sendStatisticsRequest(String accountId, String password) {
        final String token = TokenEncoder.encodeCredentials(accountId, password);

        WebClient.ResponseSpec responseSpec = client.get()
                .uri(ControllerPath.ADMINISTRATION_STATISTICS)
                .headers(httpHeaders -> httpHeaders.setBasicAuth(token))
                .retrieve()
                .onStatus(
                        status -> status == HttpStatus.UNAUTHORIZED,
                        clientResponse -> Mono.empty()
                );

        final ResponseEntity<String> responseEntity = responseSpec.toEntity(String.class).block();

        List<UrlShortener> receivedURLs = new ArrayList<>();

        if (responseEntity == null || responseEntity.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            return receivedURLs;
        }

        final Map response = responseSpec.bodyToMono(Map.class).block();

        if (response == null) {
            return receivedURLs;
        }

        for (Object urlObject : response.keySet()) {
            String url = urlObject.toString();
            int redirects = (int) response.get(url);
            UrlShortener newUrl = new UrlShortener();
            newUrl.setUrl(url);
            newUrl.setRedirects(redirects);
            receivedURLs.add(newUrl);
        }

        return receivedURLs;
    }

    public ResponseEntity<Void> sendRedirectRequest(String shortUrl) {
        final String urlApi = ControllerPath.REDIRECTION_SHORT_URL + shortUrl;

        WebClient.ResponseSpec responseSpec = client.get()
                .uri(urlApi)
                .retrieve()
                .onStatus(
                        status -> status == HttpStatus.NOT_FOUND,
                        clientResponse -> Mono.empty()
                );

        final UrlShortener urlShortener = responseSpec.bodyToMono(UrlShortener.class).block();

        if (urlShortener == null) {
            return ResponseEntity.status(404).build();
        }

        final String url = urlShortener.getUrl();
        final int redirectType = urlShortener.getRedirectType();

        if (url == null) {
            return ResponseEntity.status(404).build();
        }

        return ResponseEntity.status(redirectType).location(URI.create(url)).build();
    }
}
