package com.example.restapi.controller;

import com.example.core.ControllerPath;
import com.example.core.utils.ResponseReader;
import com.example.core.utils.TokenEncoder;
import com.example.restapi.ShortyApplication;
import com.example.restapi.exception.ExceptionMessages;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.HashMap;
import java.util.Map;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = ShortyApplication.class
)
@TestPropertySource(locations = "classpath:application-test.properties")
public class RedirectIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void redirectURLTestMissingShortUrl() throws Exception {
        ResultActions response = mockMvc.perform(get(ControllerPath.REDIRECTION)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(print());
    }

    @Test
    void redirectURLTestNotFound() throws Exception {
        String shortUrl = "abcdefg";

        ResultActions response = mockMvc.perform(get(ControllerPath.REDIRECTION)
                .contentType(MediaType.APPLICATION_JSON)
                .param("shortUrl", shortUrl));

        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(ExceptionMessages.URL_NOT_FOUND)))
                .andDo(print());
    }

    @Test
    void redirectURLTestSuccess() throws Exception {
        String accountId = "karlo";
        String url = "www.google.com";
        int redirectType = 302;

        Map<String, Object> requestMapRegister = new HashMap<>();
        requestMapRegister.put("accountId", accountId);

        MvcResult resultRegister = mockMvc.perform(post(ControllerPath.ADMINISTRATION_REGISTER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestMapRegister))).andReturn();

        String contentRegister = resultRegister.getResponse().getContentAsString();
        String password = ResponseReader.getPasswordFromRegisterResponse(contentRegister);

        String token = TokenEncoder.getBasicAuthorizationToken(accountId, password);

        Map<String, Object> requestMapShort = new HashMap<>();
        requestMapShort.put("url", url);
        requestMapShort.put("redirectType", redirectType);

        MvcResult resultShort = mockMvc.perform(post(ControllerPath.ADMINISTRATION_SHORT)
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestMapShort))).andReturn();

        String contentShort = resultShort.getResponse().getContentAsString();
        String shortUrl = ResponseReader.getShortUrlFromShortingResponse(contentShort);

        ResultActions response = mockMvc.perform(get(ControllerPath.REDIRECTION)
                .contentType(MediaType.APPLICATION_JSON)
                .param("shortUrl", shortUrl));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.url", CoreMatchers.is(url)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.redirectType", CoreMatchers.is(redirectType)))
                .andDo(print());
    }
}
