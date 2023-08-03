package com.example.shorty.utils;

import java.util.Base64;

public class TokenEncoder {
    public static final String BASIC_TOKEN_START = "Basic ";

    public static String getBasicAuthorizationToken(String id, String password) {
        final String encoded = encodeCredentials(id, password);

        return BASIC_TOKEN_START + encoded;
    }

    public static String encodeCredentials(String id, String password) {
        final String sensitive = id + ":" + password;

        return Base64.getEncoder().encodeToString(sensitive.getBytes());
    }

    public static String[] decodeBasicToken(String token) {
        final String encodedString = token.replaceFirst(BASIC_TOKEN_START, "");

        final byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        final String decodedString = new String(decodedBytes);

        return decodedString.split(":", 2);
    }
}
