package com.example.shorty.exception;

public class ExceptionMessages {
    public final static String MISSING_ACCOUNT_ID = "Missing 'accountId' field in the request body";
    public final static String MISSING_PASSWORD = "Missing 'password' field in the request body";
    public final static String UNAUTHORIZED = "Basic authorization token in request header is not valid";
    public final static String MISSING_URL = "Missing 'url' field in the request body";
    public final static String MISSING_SHORT_URL = "Missing 'shortUrl' field in the request body";
    public final static String URL_NOT_FOUND = "No URL matches the short URL given";
}
