package com.example.shorty.generator;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.fail;

class StringGeneratorTest {

    @Test
    void generateUrlTest() {
        String urlStart = "https://shorty.com/";

        String url = StringGenerator.generateUrl(urlStart);

        if (!url.startsWith(urlStart)) {
            fail("short URL not valid - " + url);
        }

        String urlEnding = url.replace("https://shorty.com/", "");

        int urlEndSize = urlEnding.length();
        assertThat(urlEndSize).isEqualTo(7);

        boolean valid = true;
        for (char letter : urlEnding.toCharArray()) {
            if (StringGenerator.urlCharacters.indexOf(letter) == -1) {
                valid = false;
                break;
            }
        }

        assertThat(valid).isEqualTo(true);
    }

    @Test
    void generatePasswordTest() {
        String password = StringGenerator.generatePassword();

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