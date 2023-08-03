package com.example.shorty.restapi;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AccountTest {

    @Test
    void getId() {
        long testLong = 1L;
        Account account = new Account();
        account.setId(testLong);

        assertThat(account.getId()).isEqualTo(testLong);
    }
}