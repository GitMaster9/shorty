package com.example.shorty.account;

import com.example.shorty.repository.AccountRepository;
import com.example.shorty.restapi.Account;
import com.example.shorty.service.AccountService;
import com.example.shorty.utils.ResponseHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;
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
    void addNewAccountTestBadRequest() {
        // given
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("account", "karlo1");

        // when
        Account account = underTest.addNewAccount(requestMap);

        // then
        assertThat(account).isNull();
    }

    @Test
    void addNewAccountTestSuccessful() {
        // given
        String accountId = "karlo1";
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("accountId", accountId);

        // when
        Account account = underTest.addNewAccount(requestMap);

        // then
        assertThat(account).isNotNull();
    }

    @Test
    void addNewAccountTestFail() {
        // given
        String accountId = "karlo1";
        String password = "password";
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("accountId", accountId);

        given(repository.findByAccountId(accountId)).willReturn(new Account(accountId, password));
        Account account = underTest.addNewAccount(requestMap);

        assertThat(account).isNull();
    }

    @Test
    void loginAccountTestBadRequestNoAccountId() {
        // given
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("account", "karlo1");
        requestMap.put("password", "pwd");

        // when
        boolean success = underTest.loginAccount(requestMap);

        // then
        assertThat(success).isEqualTo(false);
    }

    @Test
    void loginAccountTestBadRequestNoPassword() {
        // given
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("accountId", "karlo1");
        requestMap.put("pwd", "pwd");

        // when
        boolean success = underTest.loginAccount(requestMap);

        // then
        assertThat(success).isEqualTo(false);
    }

    @Test
    void loginAccountTestFail() {
        // given
        String accountId = "karlo1";
        String password = "pwd";
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("accountId", accountId);
        requestMap.put("password", password);

        // when
        boolean success = underTest.loginAccount(requestMap);

        // then
        assertThat(success).isEqualTo(false);
    }

    @Test
    void loginAccountTestSuccess() {
        // given
        String accountId = "karlo1";
        String password = "password";
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("accountId", accountId);
        requestMap.put("password", password);

        // when
        given(repository.findByAccountIdAndPassword(accountId, password)).willReturn(new Account(accountId, password));

        boolean success = underTest.loginAccount(requestMap);

        // then
        assertThat(success).isEqualTo(true);
    }
}