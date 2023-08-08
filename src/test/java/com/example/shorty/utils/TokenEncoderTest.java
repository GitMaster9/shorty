package com.example.shorty.utils;

import com.example.shorty.restapi.Account;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class TokenEncoderTest {

    @Test
    void encodeBasicTokenTest() {
        String id = "john";
        String password = "123456";

        String result = TokenEncoder.getBasicAuthorizationToken(id, password);

        String expected = "Basic am9objoxMjM0NTY=";

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void encodeCredentialsTest() {
        String id = "john";
        String password = "123456";

        String result = TokenEncoder.encodeCredentials(id, password);

        String expected = "am9objoxMjM0NTY=";

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void decodeBasicTokenTest() {
        String token = "Basic am9objoxMjM0NTY=";
        String id = "john";
        String password = "123456";

        String[] result = TokenEncoder.decodeBasicToken(token);

        String[] expected = {id, password};

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void getAccountFromTokenTestFail() {
        String token = "Invalid";

        Account found = TokenEncoder.getAccountFromToken(token);
        assertThat(found).isNull();
    }

    @Test
    void getAccountFromTokenTestSuccess() {
        String accountId = "john";
        String password = "123456";
        String token = TokenEncoder.getBasicAuthorizationToken(accountId, password);

        Account expected = new Account();
        expected.setAccountId(accountId);
        expected.setPassword(password);

        Account found = TokenEncoder.getAccountFromToken(token);
        assertThat(found).usingRecursiveComparison().isEqualTo(expected);
    }
}