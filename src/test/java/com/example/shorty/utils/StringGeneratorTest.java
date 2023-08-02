package com.example.shorty.utils;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class StringGeneratorTest {

    @Test
    void generateUrlTest() {
        String url = StringGenerator.generateRandomString(StringGeneratorType.URL);

        int urlEndSize = url.length();
        assertThat(urlEndSize).isEqualTo(7);

        boolean valid = true;
        for (char letter : url.toCharArray()) {
            if (StringGenerator.urlCharacters.indexOf(letter) == -1) {
                valid = false;
                break;
            }
        }

        assertThat(valid).isEqualTo(true);
    }

    @Test
    void generatePasswordTest() {
        String password = StringGenerator.generateRandomString(StringGeneratorType.PASSWORD);

        int urlEndSize = password.length();
        assertThat(urlEndSize).isEqualTo(10);

        boolean valid = true;
        for (char letter : password.toCharArray()) {
            if (StringGenerator.passwordCharacters.indexOf(letter) == -1) {
                valid = false;
                break;
            }
        }

        assertThat(valid).isEqualTo(true);
    }
}