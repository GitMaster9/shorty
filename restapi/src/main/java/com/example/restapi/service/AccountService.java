package com.example.restapi.service;

import com.example.core.model.Account;
import com.example.core.model.KeycloakUser;
import com.example.repository.AccountRepository;
import com.example.core.utils.StringGenerator;
import com.example.core.utils.StringGeneratorType;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@ComponentScan(basePackageClasses = AccountRepository.class)
public class AccountService {

    @Value("${keycloakclient.client-id}")
    private String CLIENT_ID;

    @Value("${keycloakclient.client-secret}")
    private String CLIENT_SECRET;

    @Value("${keycloakclient.token-url}")
    private String TOKEN_FULL_URL;

    @Value("${keycloakclient.users-url}")
    private String USERS_URL;

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
            System.out.println("NIJE USPIO");
            return null;
        }
        System.out.println("USPIO");

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
        String accessToken = getAdminToken();
        if (accessToken == null) {
            return false;
        }

        KeycloakUser user = KeycloakUser.getUserWithJsonValues(username, password);

        Gson gson = new Gson();
        String jsonString = gson.toJson(user);

        WebClient.ResponseSpec responseSpecCreateUser = WebClient.create(USERS_URL)
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

        if (responseEntity == null || responseEntity.getStatusCode() != HttpStatus.CREATED) {
            return false;
        }

        WebClient.ResponseSpec responseSpecGetId = WebClient.create(USERS_URL)
                .get()
                .headers(httpHeaders -> httpHeaders.setBearerAuth(accessToken))
                .retrieve()
                .onStatus(
                        status -> status == HttpStatus.UNAUTHORIZED,
                        clientResponse -> Mono.empty()
                );

        final ResponseEntity<String> responseEntityGetId = responseSpecGetId.toEntity(String.class).block();

        if (responseEntityGetId == null || responseEntityGetId.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            System.out.println("NE MOZE NACI USER ID ALI JE NAPRAVIO USERA");
            return false;
        }

        String bodyContent = responseEntityGetId.getBody();

        Object[] objectArray = gson.fromJson(bodyContent, Object[].class);

        String id = null;

        for (Object object : objectArray) {
            Map<String, Object> data = (Map<String, Object>) object;
            String tmpUsername = (String) data.get("username");

            if (tmpUsername.equals(username)) {
                id = (String) data.get("id");
                break;
            }
        }

        String ROLE_URI = USERS_URL + "/" + id + "/role-mappings/clients/6574535d-4e16-4ae4-a73f-7c5c76922557";

        List<Map<String, Object>> dummyList = new ArrayList<>();

        Map<String, Object> roleData = new HashMap<>();
        roleData.put("id", "22b8966c-8f91-4252-b493-60e6450192b1");
        roleData.put("name", "client_user");

        dummyList.add(roleData);

        WebClient.ResponseSpec responseSpecAddRole = WebClient.create(ROLE_URI)
                .post()
                .headers(httpHeaders -> httpHeaders.setBearerAuth(accessToken))
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(dummyList), List.class)
                .retrieve();

        final ResponseEntity<String> responseEntityAddRole = responseSpecAddRole.toEntity(String.class).block();
        System.out.println(responseEntityAddRole);

        return true;
    }

    @SuppressWarnings("DuplicatedCode")
    public String getAdminToken() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "client_credentials");
        formData.add("client_id", CLIENT_ID);
        formData.add("client_secret", CLIENT_SECRET);

        WebClient.ResponseSpec responseSpec = WebClient.create(TOKEN_FULL_URL)
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
