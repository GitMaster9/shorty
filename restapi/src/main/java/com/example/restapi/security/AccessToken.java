package com.example.restapi.security;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.HashMap;

public class AccessToken {
    @SuppressWarnings("DuplicatedCode")
    public static String getUserToken(String username, String password) {
        final MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "password");
        formData.add("client_id", KeycloakConfig.CLIENT_ID);
        formData.add("client_secret", KeycloakConfig.CLIENT_SECRET);
        formData.add("username", username);
        formData.add("password", password);

        final WebClient.ResponseSpec responseSpec = WebClient.create(KeycloakConfig.TOKEN_FULL_URL)
                .post()
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .onStatus(
                        status -> status == HttpStatus.UNAUTHORIZED,
                        clientResponse -> Mono.empty()
                );

        final HashMap response = responseSpec.bodyToMono(HashMap.class).block();

        if (response == null) {
            return null;
        }

        return (String) response.get("access_token");
    }
}
