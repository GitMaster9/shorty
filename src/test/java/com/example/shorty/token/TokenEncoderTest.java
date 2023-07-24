package com.example.shorty.token;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class TokenEncoderTest {

    @Test
    void encodeBasicToken() {
        String id = "john";
        String password = "123456";

        String result = TokenEncoder.encodeBasicToken(id, password);

        String expected = "Basic am9objoxMjM0NTY=";

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void decodeBasicToken() {
        String token = "Basic am9objoxMjM0NTY=";
        String id = "john";
        String password = "123456";

        String[] result = TokenEncoder.decodeBasicToken(token);

        String[] expected = {id, password};

        assertThat(result).isEqualTo(expected);
    }
}