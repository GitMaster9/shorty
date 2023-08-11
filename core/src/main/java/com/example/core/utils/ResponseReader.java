package com.example.core.utils;

public class ResponseReader {
    public static String getPasswordFromRegisterResponse(String response) {
        String reducedResponse = response.replace("{\"password\":\"", "");
        int removeIndex = reducedResponse.indexOf('"');

        return reducedResponse.substring(0, removeIndex);
    }

    public static String getShortUrlFromShortingResponse(String response) {
        String reducedResponse = response.replace("{\"shortUrl\":\"", "");
        int removeIndex = reducedResponse.indexOf('"');

        return reducedResponse.substring(0, removeIndex);
    }
}
