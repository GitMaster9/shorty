package com.example.shorty.service;

import com.example.shorty.repository.AccountRepository;
import com.example.shorty.restapi.Account;
import com.example.shorty.utils.StringGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    @Mock
    private AccountRepository repository;
    private AccountService underTest;

    @BeforeEach
    void setUp() {
        underTest = new AccountService(repository);
    }

    @Test
    void addNewAccountTestFail() {
        String accountId = "karlo1";
        String password = "password";

        Account exists = new Account();
        exists.setAccountId(accountId);
        exists.setPassword(password);

        given(repository.findByAccountId(accountId)).willReturn(exists);
        Account found = underTest.addNewAccount(accountId);

        assertThat(found).isNull();
    }

    @Test
    void addNewAccountTestSuccess() {
        String accountId = "karlo1";

        given(repository.findByAccountId(accountId)).willReturn(null);
        Account account = underTest.addNewAccount(accountId);

        assertThat(account).isNotNull();
        assertThat(account.getAccountId()).isEqualTo(accountId);

        int urlSize = account.getPassword().length();
        assertThat(urlSize).isEqualTo(StringGenerator.passwordStringSize);
    }

    @Test
    void loginAccountTestFail() {
        String accountId = "karlo1";
        String password = "pwd";

        given(repository.findByAccountIdAndPassword(accountId, password)).willReturn(null);
        Account found = underTest.loginAccount(accountId, password);

        assertThat(found).isNull();
    }

    @Test
    void loginAccountTestSuccess() {
        String accountId = "karlo1";
        String password = "password";

        Account exists = new Account();
        exists.setAccountId(accountId);
        exists.setPassword(password);

        given(repository.findByAccountIdAndPassword(accountId, password)).willReturn(exists);
        Account found = underTest.loginAccount(accountId, password);

        assertThat(found).isNotNull();
        assertThat(found.getAccountId()).isEqualTo(accountId);
        assertThat(found.getPassword()).isEqualTo(password);
    }
}