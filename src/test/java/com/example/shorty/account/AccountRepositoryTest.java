package com.example.shorty.account;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    private AccountRepository underTest;


    @Test
    void checkTestFind() {
        String accountId = "karlo";

        Account testAccount = new Account(accountId, "password");
        underTest.save(testAccount);

        Account account = underTest.findAccountById(accountId);

        assertThat(account).isEqualTo(testAccount);
    }

    @Test
    void checkTestNotFind() {
        String accountId = "karlo";

        Account account = underTest.findAccountById(accountId);

        assertThat(account).isNull();
    }
}