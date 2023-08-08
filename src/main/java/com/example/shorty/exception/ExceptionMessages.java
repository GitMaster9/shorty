package com.example.shorty.exception;

public final class ExceptionMessages {
    public final static String MISSING_ACCOUNT_ID = "Missing 'accountId' field in the request body";
    public final static String MISSING_PASSWORD = "Missing 'password' field in the request body";
    public final static String MISSING_URL = "Missing 'url' field in the request body";
    public final static String UNAUTHORIZED = "Account with this user credentials not found";
    public final static String URL_NOT_FOUND = "No URL matches the short URL given";
    public final static String BAD_TOKEN = "Basic authorization token is not valid";
    //public final static String MISSING_SHORT_URL = "Missing 'shortUrl' field in the request body";
}
