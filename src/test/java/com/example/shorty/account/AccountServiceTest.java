package com.example.shorty.account;

import com.example.shorty.responsehandler.ResponseHandler;
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
        ResponseEntity<Object> responseEntity = underTest.addNewAccount(requestMap);

        // then
        Map<String, String> data = new HashMap<>();
        data.put("description", "Failed - no 'accountId' field in request body");
        ResponseEntity<Object> expectedResponseEntity = new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);

        assertThat(responseEntity).isEqualTo(expectedResponseEntity);
    }

    @Test
    void addNewAccountTestSuccessful() {
        // given
        String accountId = "karlo1";
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("accountId", accountId);

        // when
        ResponseEntity<Object> responseEntity = underTest.addNewAccount(requestMap);

        Object success = ResponseHandler.getDataFieldFromResponse(responseEntity, "success");

        // then
        assertThat(success).isEqualTo(true);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void addNewAccountTestFail() {
        // given
        String accountId = "karlo1";
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("accountId", accountId);

        given(repository.findAccountById(accountId)).willReturn(new Account(accountId, "password"));

        ResponseEntity<Object> responseEntity = underTest.addNewAccount(requestMap);

        Object success = ResponseHandler.getDataFieldFromResponse(responseEntity, "success");
        assertThat(success).isEqualTo(false);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void createRegisterSuccessResponseTest() {
        String password = "dummypassword";

        ResponseEntity<Object> responseEntity = underTest.createRegisterSuccessResponse(password);

        Map<String, Object> data = new HashMap<>();
        data.put("success", true);
        data.put("password", password);

        ResponseEntity<Object> expectedResponseEntity = new ResponseEntity<>(data, HttpStatus.OK);

        assertThat(responseEntity).isEqualTo(expectedResponseEntity);
    }

    @Test
    void loginAccountTestBadRequestNoAccountId() {
        // given
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("account", "karlo1");
        requestMap.put("password", "pwd");

        // when
        ResponseEntity<Object> responseEntity = underTest.loginAccount(requestMap);

        // then
        Map<String, String> data = new HashMap<>();
        data.put("description", "Failed - no 'accountId' field in request body");
        ResponseEntity<Object> expectedResponseEntity = new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);

        assertThat(responseEntity).isEqualTo(expectedResponseEntity);
    }

    @Test
    void loginAccountTestBadRequestNoPassword() {
        // given
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("accountId", "karlo1");
        requestMap.put("pwd", "pwd");

        // when
        ResponseEntity<Object> responseEntity = underTest.loginAccount(requestMap);

        // then
        Map<String, String> data = new HashMap<>();
        data.put("description", "Failed - no 'password' field in request body");
        ResponseEntity<Object> expectedResponseEntity = new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);

        assertThat(responseEntity).isEqualTo(expectedResponseEntity);
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
        ResponseEntity<Object> responseEntity = underTest.loginAccount(requestMap);

        // then
        Object success = ResponseHandler.getDataFieldFromResponse(responseEntity, "success");
        assertThat(success).isEqualTo(false);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
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
        //given(repository.findAccountById(accountId)).willReturn(Optional.of(new Account(accountId, password)));
        given(repository.findAccountByIdAndPassword(accountId, password)).willReturn(new Account(accountId, password));

        ResponseEntity<Object> responseEntity = underTest.loginAccount(requestMap);

        // then
        Object success = ResponseHandler.getDataFieldFromResponse(responseEntity, "success");
        assertThat(success).isEqualTo(true);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void createSuccessStatusResponseTest() {
        boolean success = false;

        ResponseEntity<Object> responseEntity = underTest.createSuccessStatusResponse(success);

        Map<String, Object> data = new HashMap<>();
        data.put("success", success);

        ResponseEntity<Object> expectedResponseEntity = new ResponseEntity<>(data, HttpStatus.OK);

        assertThat(responseEntity).isEqualTo(expectedResponseEntity);
    }

    @Test
    void createRequestFailResponseTest() {
        String description = "some description";

        ResponseEntity<Object> responseEntity = underTest.createRequestFailResponse(description);

        Map<String, Object> data = new HashMap<>();
        data.put("description", description);

        ResponseEntity<Object> expectedResponseEntity = new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);

        assertThat(responseEntity).isEqualTo(expectedResponseEntity);
    }
}