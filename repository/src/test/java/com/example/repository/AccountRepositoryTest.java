package com.example.repository;

import com.example.core.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    private AccountRepository underTest;

//    @Test
//    void checkTestFind() {
//        String accountId = "karlo";
//
//        Account testAccount = new Account();
//        testAccount.setAccountId(accountId);
//        testAccount.setPassword("password");
//        underTest.save(testAccount);
//
//        Account account = underTest.findByAccountId(accountId);
//
//        assertThat(account).isEqualTo(testAccount);
//    }
//
//    @Test
//    void checkTestNotFind() {
//        String accountId = "karlo";
//
//        Account account = underTest.findByAccountId(accountId);
//
//        assertThat(account).isNull();
//    }
}