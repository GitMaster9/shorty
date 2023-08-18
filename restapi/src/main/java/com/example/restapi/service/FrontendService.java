package com.example.restapi.service;

import com.example.core.model.Account;
import com.example.core.model.ShortingResponse;
import com.example.core.model.UrlShortener;
import com.example.core.ControllerPath;
import com.example.restapi.security.AccessToken;
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

    public Account sendRegisterRequest(String accountId) {
        final Map<String, String> data = new HashMap<>();
        data.put("accountId", accountId);

        final String urlApi = ControllerPath.API_URL_BASE + ControllerPath.ADMINISTRATION_REGISTER;

        final WebClient.ResponseSpec responseSpec = WebClient.create(urlApi)
                .post()
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

        final String urlApi = ControllerPath.API_URL_BASE + ControllerPath.ADMINISTRATION_LOGIN;

        final WebClient.ResponseSpec responseSpec = WebClient.create(urlApi)
                .post()
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
        final ShortingResponse shortingResponse = new ShortingResponse();

        final String accessToken = AccessToken.getUserToken(accountId, password);
        if (accessToken == null) {
            shortingResponse.setDescription("Can't get user access token - possible wrong user credential input");
            return shortingResponse;
        }

        final Map<String, Object> data = new HashMap<>();
        data.put("url", url);
        data.put("redirectType", redirectType);

        final String urlApi = ControllerPath.API_URL_BASE + ControllerPath.ADMINISTRATION_SHORT;

        final WebClient.ResponseSpec responseSpec = WebClient.create(urlApi)
                .post()
                .headers(httpHeaders -> httpHeaders.setBearerAuth(accessToken))
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(data), HashMap.class)
                .retrieve()
                .onStatus(
                        status -> status == HttpStatus.UNAUTHORIZED,
                        clientResponse -> Mono.empty()
                );

        final ResponseEntity<String> responseEntity = responseSpec.toEntity(String.class).block();

        if (responseEntity == null || responseEntity.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            shortingResponse.setDescription("User credentials not valid");
            return shortingResponse;
        }

        final HashMap response = responseSpec.bodyToMono(HashMap.class).block();

        if (response == null) {
            shortingResponse.setDescription("Api response is null");
            return shortingResponse;
        }

        final Object descriptionObject = response.get("description");
        if (descriptionObject != null) {
            shortingResponse.setDescription(descriptionObject.toString());
            return shortingResponse;
        }

        final Object shortUrlObject = response.get("shortUrl");
        if (shortUrlObject == null) {
            shortingResponse.setDescription("ERROR: no short url");
            return shortingResponse;
        }

        shortingResponse.setShortUrl(shortUrlObject.toString());
        shortingResponse.setDescription(null);

        return  shortingResponse;
    }

    public List<UrlShortener> sendStatisticsRequest(String accountId, String password) {
        final String accessToken = AccessToken.getUserToken(accountId, password);

        final List<UrlShortener> receivedURLs = new ArrayList<>();
        if (accessToken == null) {
            return receivedURLs;
        }

        final String urlApi = ControllerPath.API_URL_BASE + ControllerPath.ADMINISTRATION_STATISTICS;

        final WebClient.ResponseSpec responseSpec = WebClient.create(urlApi)
                .get()
                .headers(httpHeaders -> httpHeaders.setBearerAuth(accessToken))
                .retrieve()
                .onStatus(
                        status -> status == HttpStatus.UNAUTHORIZED,
                        clientResponse -> Mono.empty()
                );

        final ResponseEntity<String> responseEntity = responseSpec.toEntity(String.class).block();

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
        final String urlApi = ControllerPath.API_URL_BASE + ControllerPath.REDIRECTION_SHORT_URL + shortUrl;

        final WebClient.ResponseSpec responseSpec = WebClient.create(urlApi)
                .get()
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
