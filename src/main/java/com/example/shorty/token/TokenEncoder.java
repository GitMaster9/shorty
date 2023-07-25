package com.example.shorty.token;

import java.util.Base64;

public class TokenEncoder {
    public static final String basicTokenStart = "Basic ";

    public static String encodeBasicToken(String id, String password) {
        String sensitive = id + ":" + password;
        String encoded = Base64.getEncoder().encodeToString(sensitive.getBytes());

        return basicTokenStart + encoded;
    }

    public static String[] decodeBasicToken(String token) {
        String encodedString = token.replaceFirst(basicTokenStart, "");

        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        String decodedString = new String(decodedBytes);

        return decodedString.split(":", 2);
    }
}
