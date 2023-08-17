package com.example.restapi.security;

public class KeycloakConfig {

    public static final String CLIENT_ID = "shorty-rest-api";

    public static final String CLIENT_SECRET = "hCtvISAhfndkC1LPL9EXsUmSbTsOS4YA";
    private static final String KC_BASE_URL = "http://localhost:8081/realms/shorty";
    private static final String KC_ADMIN_BASE_URL = "http://localhost:8081/admin/realms/shorty";

    public static final String TOKEN_FULL_URL = KC_BASE_URL + "/protocol/openid-connect/token";

    public static final String USERS_URL = KC_ADMIN_BASE_URL + "/users";
    public static final String PRINCIPLE_ATTRIBUTE = "preferred_username";
    public static final String ROLE_USER_NAME = "client_user";
    public static final String ROLE_USER_ID = "22b8966c-8f91-4252-b493-60e6450192b1";
    private static final String CLIENT_KC_ID = "6574535d-4e16-4ae4-a73f-7c5c76922557";
    public static final String ROLE_URL_END = "/role-mappings/clients/" + CLIENT_KC_ID;
}
