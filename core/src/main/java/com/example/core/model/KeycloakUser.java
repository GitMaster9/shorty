package com.example.core.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
public class KeycloakUser {
    private String username;
    private boolean enabled;
    private List<Map<String, Object>> credentials;

    public static KeycloakUser getUserWithJsonValues(String username, String password) {
        KeycloakUser user = new KeycloakUser();

        user.setUsername(username);
        user.setEnabled(true);

        Map<String, Object> credentialsData = new HashMap<>();
        credentialsData.put("type", "password");
        credentialsData.put("value", password);
        credentialsData.put("temporary", false);

        List<Map<String, Object>> credentials = new ArrayList<>();
        credentials.add(credentialsData);

        user.setCredentials(credentials);

        return user;
    }
}
