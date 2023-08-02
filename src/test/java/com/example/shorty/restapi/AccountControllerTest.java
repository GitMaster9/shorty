package com.example.shorty.restapi;

import com.example.shorty.exception.ExceptionMessages;
import com.example.shorty.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.HashMap;
import java.util.Map;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(controllers = AccountController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerTestMissingAccountId() throws Exception {
        String accountId = "karlo";

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("account", accountId);

        given(accountService.addNewAccount(accountId)).willReturn(null);

        ResultActions response = mockMvc.perform(post(ControllerPath.ADMINISTRATION_REGISTER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestMap)));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(ExceptionMessages.MISSING_ACCOUNT_ID)))
                .andDo(print());
    }

    @Test
    void registerTestFail() throws Exception {
        String accountId = "karlo";

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("accountId", accountId);

        given(accountService.addNewAccount(accountId)).willReturn(null);

        ResultActions response = mockMvc.perform(post(ControllerPath.ADMINISTRATION_REGISTER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestMap)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", CoreMatchers.is(false)))
                .andDo(print());
    }

    @Test
    void registerTestSuccess() throws Exception {
        String accountId = "karlo";
        String password = "password";

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("accountId", accountId);

        Account givenAccount = new Account();
        givenAccount.setAccountId(accountId);
        givenAccount.setPassword(password);

        given(accountService.addNewAccount(accountId)).willReturn(givenAccount);

        ResultActions response = mockMvc.perform(post(ControllerPath.ADMINISTRATION_REGISTER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestMap)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", CoreMatchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password", CoreMatchers.is(password)))
                .andDo(print());
    }

    @Test
    void loginTestMissingAccountId() throws Exception {
        String accountId = "karlo";
        String password = "password";

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("password", password);

        given(accountService.addNewAccount(accountId)).willReturn(null);

        ResultActions response = mockMvc.perform(post(ControllerPath.ADMINISTRATION_LOGIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestMap)));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(ExceptionMessages.MISSING_ACCOUNT_ID)))
                .andDo(print());
    }

    @Test
    void loginTestMissingPassword() throws Exception {
        String accountId = "karlo";

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("accountId", accountId);

        given(accountService.addNewAccount(accountId)).willReturn(null);

        ResultActions response = mockMvc.perform(post(ControllerPath.ADMINISTRATION_LOGIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestMap)));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(ExceptionMessages.MISSING_PASSWORD)))
                .andDo(print());
    }

    @Test
    void loginTestFail() throws Exception {
        String accountId = "karlo";
        String password = "password";

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("accountId", accountId);
        requestMap.put("password", password);

        given(accountService.loginAccount(accountId, password)).willReturn(null);

        ResultActions response = mockMvc.perform(post(ControllerPath.ADMINISTRATION_LOGIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestMap)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", CoreMatchers.is(false)))
                .andDo(print());
    }

    @Test
    void loginTestSuccess() throws Exception {
        String accountId = "karlo";
        String password = "password";

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("accountId", accountId);
        requestMap.put("password", password);

        Account givenAccount = new Account();
        givenAccount.setAccountId(accountId);
        givenAccount.setPassword(password);

        given(accountService.loginAccount(accountId, password)).willReturn(givenAccount);

        ResultActions response = mockMvc.perform(post(ControllerPath.ADMINISTRATION_LOGIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestMap)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", CoreMatchers.is(true)))
                .andDo(print());
    }
}