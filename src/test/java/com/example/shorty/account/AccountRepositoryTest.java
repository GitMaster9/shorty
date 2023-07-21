package com.example.shorty.account;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    private AccountRepository underTest;

    @Test
    void checkIfFindAccountByAccountId() {
        //given
        String accountId = "karlo";
        Account account = new Account(accountId, "sifra");
        underTest.save(account);

        // when
        Optional<Account> exists =  underTest.findAccountByAccountId(accountId);

        // then
        assertThat(exists).isPresent();
    }

    @Test
    void checkIfDoesNotFindAccountByAccountId() {
        //given
        String accountId = "karlo";

        // when
        Optional<Account> exists = underTest.findAccountByAccountId(accountId);

        // then
        assertThat(exists).isEmpty();
    }
}