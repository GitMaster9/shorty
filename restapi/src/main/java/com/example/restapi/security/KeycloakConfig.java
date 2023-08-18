package com.example.restapi.security;

public class KeycloakConfig {

    public static final String CLIENT_ID = "shorty-rest-api";
    public static final String CLIENT_SECRET = "hCtvISAhfndkC1LPL9EXsUmSbTsOS4YA";
    private static final String PORT_BASE_URL = "http://localhost:8081";
    private static final String KC_BASE_URL = PORT_BASE_URL + "/realms/shorty";
    private static final String KC_ADMIN_BASE_URL = PORT_BASE_URL + "/admin/realms/shorty";
    public static final String TOKEN_FULL_URL = KC_BASE_URL + "/protocol/openid-connect/token";
    public static final String USERS_URL = KC_ADMIN_BASE_URL + "/users";
    public static final String PRINCIPLE_ATTRIBUTE = "preferred_username";
    public static final String CLIENT_CREDENTIALS = "client_credentials";
}
