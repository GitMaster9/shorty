package com.example.core.utils;

import java.util.Base64;

public class TokenEncoder {
    public static final String BASIC_TOKEN_START = "Basic ";
    public static final String BEARER_TOKEN_START = "Bearer ";
    public static final String BEARER_TOKEN_USERNAME = "\"preferred_username\":\"";

    public static String getBasicAuthorizationToken(String id, String password) {
        final String encoded = encodeCredentials(id, password);

        return BASIC_TOKEN_START + encoded;
    }

    public static String encodeCredentials(String id, String password) {
        final String sensitive = id + ":" + password;

        return Base64.getEncoder().encodeToString(sensitive.getBytes());
    }

    public static String[] decodeBasicToken(String token) {
        if (isBasicTokenInvalid(token)) {
            return null;
        }

        final String encodedString = token.replaceFirst(BASIC_TOKEN_START, "");

        final byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        final String decodedString = new String(decodedBytes);

        return decodedString.split(":", 2);
    }

    public static boolean isBasicTokenInvalid(String token) {
        return !token.startsWith(BASIC_TOKEN_START);
    }

    public static boolean isBearerTokenInvalid(String token) {
        return !token.startsWith(BEARER_TOKEN_START);
    }

    public static String getPreferredUsernameFromBearerToken(String token) {
        if (isBearerTokenInvalid(token)) {
            return null;
        }

        String payload = decodePayloadFromBearerToken(token);

        final String[] parts = payload.split(BEARER_TOKEN_USERNAME, 2);
        if (parts.length != 2) {
            return null;
        }

        String name = parts[1];
        int removeIndex = name.indexOf('"');
        if (removeIndex < 0) {
            return null;
        }

        return name.substring(0, removeIndex);
    }

    public static String decodePayloadFromBearerToken(String token) {
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String[] chunks = token.split("\\.");

        return new String(decoder.decode(chunks[1]));
    }
}
