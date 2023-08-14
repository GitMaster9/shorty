package com.example.restapi.controller;

import com.example.core.ControllerPath;
import com.example.core.model.Account;
import com.example.core.model.UrlShortener;
import com.example.core.utils.TokenEncoder;
import com.example.restapi.ShortyApplication;
import com.example.restapi.UrlShortenerExample;
import com.example.restapi.exception.ExceptionMessages;
import com.example.restapi.service.UrlShortenerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest(classes = ShortyApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ExtendWith(MockitoExtension.class)
class UrlShortenerControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UrlShortenerService urlShortenerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shortURLMissingUrlTest() throws Exception {
        String accountId = "karlo";
        String password = "password";
        String token = TokenEncoder.getBasicAuthorizationToken(accountId, password);

        Map<String, Object> dummyRequestMap = new HashMap<>();

        Account exists = new Account();
        exists.setAccountId(accountId);
        exists.setPassword(password);

        given(urlShortenerService.getAuthenticatedAccount(token)).willReturn(exists);

        ResultActions response = mockMvc.perform(post(ControllerPath.ADMINISTRATION_SHORT)
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dummyRequestMap)));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(ExceptionMessages.MISSING_URL)))
                .andDo(print());
    }

    @Test
    void shortURLSuccess1Test() throws Exception {
        String accountId = "karlo";
        String password = "password";
        String token = TokenEncoder.getBasicAuthorizationToken(accountId, password);

        String url = "www.google.com";
        int redirectType = 302;
        String shortUrl = "www.shorty.com/fake";

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("url", url);
        requestMap.put("redirectType", redirectType);

        Account exists = new Account();
        exists.setAccountId(accountId);
        exists.setPassword(password);

        given(urlShortenerService.getAuthenticatedAccount(token)).willReturn(exists);
        given(urlShortenerService.shortURL(exists, url, redirectType)).willReturn(shortUrl);

        ResultActions response = mockMvc.perform(post(ControllerPath.ADMINISTRATION_SHORT)
                        .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestMap)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.shortUrl", CoreMatchers.is(shortUrl)))
                .andDo(print());
    }

    @Test
    void shortURLSuccess2Test() throws Exception {
        String accountId = "karlo";
        String password = "password";
        String token = TokenEncoder.getBasicAuthorizationToken(accountId, password);

        String url = "www.google.com";
        int redirectType = 302;
        String shortUrl = "www.shorty.com/fake";

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("url", url);

        Account exists = new Account();
        exists.setAccountId(accountId);
        exists.setPassword(password);

        given(urlShortenerService.getAuthenticatedAccount(token)).willReturn(exists);
        given(urlShortenerService.shortURL(exists, url, redirectType)).willReturn(shortUrl);

        ResultActions response = mockMvc.perform(post(ControllerPath.ADMINISTRATION_SHORT)
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestMap)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.shortUrl", CoreMatchers.is(shortUrl)))
                .andDo(print());
    }

    @Test
    void getUserStatisticsSuccess1Test() throws Exception {
        String accountId = "karlo";
        String password = "password";
        String token = TokenEncoder.getBasicAuthorizationToken(accountId, password);

        Account account = new Account();
        account.setAccountId(accountId);
        account.setPassword(password);

        given(urlShortenerService.getAuthenticatedAccount(token)).willReturn(account);

        ResultActions response = mockMvc.perform(get(ControllerPath.ADMINISTRATION_STATISTICS)
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(0)))
                .andDo(print());
    }

    @Test
    void getUserStatisticsSuccess2Test() throws Exception {
        String accountId = "karlo";
        String password = "password";
        String token = TokenEncoder.getBasicAuthorizationToken(accountId, password);

        Account account = new Account();
        account.setAccountId(accountId);
        account.setPassword(password);

        List<UrlShortener> uniqueURLs = UrlShortenerExample.getUniqueURLsExamples();

        given(urlShortenerService.getAuthenticatedAccount(token)).willReturn(account);
        given(urlShortenerService.getStatistics(account.getAccountId())).willReturn(uniqueURLs);

        ResultActions response = mockMvc.perform(get(ControllerPath.ADMINISTRATION_STATISTICS)
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(uniqueURLs.size())))
                .andDo(print());
    }

    @Test
    void getUserStatisticsUnauthorizedTest() throws Exception {
        String accountId = "karlo";
        String password = "password";
        String token = TokenEncoder.getBasicAuthorizationToken(accountId, password);

        ResultActions response = mockMvc.perform(get(ControllerPath.ADMINISTRATION_STATISTICS)
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(ExceptionMessages.UNAUTHORIZED)))
                .andDo(print());
    }
}