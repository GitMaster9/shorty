package com.example.restapi.service;

import com.example.core.model.Account;
import com.example.core.model.KeycloakUser;
import com.example.repository.AccountRepository;
import com.example.core.utils.StringGenerator;
import com.example.core.utils.StringGeneratorType;
import com.example.restapi.security.KeycloakConfig;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.HashMap;

@Service
@ComponentScan(basePackageClasses = AccountRepository.class)
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account addNewAccount(String accountId) {
        final Account account = accountRepository.findByAccountId(accountId);
        if (account != null) {
            return null;
        }

        final String password = StringGenerator.generateRandomString(StringGeneratorType.PASSWORD);

        if (!createKeycloakUser(accountId, password)) {
            return null;
        }

        final Account newAccount = new Account();
        newAccount.setAccountId(accountId);
        newAccount.setPassword(password);

        accountRepository.save(newAccount);

        return newAccount;
    }

    public Account loginAccount(String accountId, String password) {
        return accountRepository.findByAccountIdAndPassword(accountId, password);
    }

    public boolean createKeycloakUser(String username, String password) {
        final String accessToken = getAdminToken();
        if (accessToken == null) {
            return false;
        }

        final KeycloakUser user = KeycloakUser.getUserWithJsonValues(username, password);

        final Gson gson = new Gson();
        final String jsonString = gson.toJson(user);

        final WebClient.ResponseSpec responseSpecCreateUser = WebClient.create(KeycloakConfig.USERS_URL)
                .post()
                .headers(httpHeaders -> httpHeaders.setBearerAuth(accessToken))
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(jsonString), String.class)
                .retrieve()
                .onStatus(
                        status -> status == HttpStatus.BAD_REQUEST,
                        clientResponse -> Mono.empty()
                );

        final ResponseEntity<String> responseEntity = responseSpecCreateUser.toEntity(String.class).block();

        return responseEntity != null && responseEntity.getStatusCode() == HttpStatus.CREATED;
    }

    @SuppressWarnings("DuplicatedCode")
    public String getAdminToken() {
        final MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", KeycloakConfig.CLIENT_CREDENTIALS);
        formData.add("client_id", KeycloakConfig.CLIENT_ID);
        formData.add("client_secret", KeycloakConfig.CLIENT_SECRET);

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
