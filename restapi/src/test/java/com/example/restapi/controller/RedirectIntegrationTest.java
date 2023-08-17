package com.example.restapi.controller;

import com.example.core.ControllerPath;
import com.example.core.utils.ResponseReader;
import com.example.core.utils.TokenEncoder;
import com.example.restapi.ShortyApplication;
import com.example.restapi.exception.ExceptionMessages;
import com.example.restapi.security.AccessToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
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
    void redirectURLMissingShortUrlTest() throws Exception {
        ResultActions response = mockMvc.perform(get(ControllerPath.REDIRECTION)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(print());
    }

    @Test
    void redirectURLNotFoundTest() throws Exception {
        String shortUrl = "shortUrlRedirectURLNotFoundTest";

        ResultActions response = mockMvc.perform(get(ControllerPath.REDIRECTION)
                .contentType(MediaType.APPLICATION_JSON)
                .param("shortUrl", shortUrl));

        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(ExceptionMessages.URL_NOT_FOUND)))
                .andDo(print());
    }

    @Disabled
    @Test
    void redirectURLSuccessTest() throws Exception {
        String accountId = "userRedirectURLTestSuccess";
        String url = "urlRedirectURLTestSuccess";

        Map<String, Object> requestMapRegister = new HashMap<>();
        requestMapRegister.put("accountId", accountId);

        MvcResult resultRegister = mockMvc.perform(post(ControllerPath.ADMINISTRATION_REGISTER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestMapRegister)))
                .andDo(print())
                .andReturn();

        String contentRegister = resultRegister.getResponse().getContentAsString();
        String password = ResponseReader.getPasswordFromRegisterResponse(contentRegister);

        String token = AccessToken.getUserToken(accountId, password);
        System.out.println("TOKEN = " + token);

        Map<String, Object> requestMapShort = new HashMap<>();
        requestMapShort.put("url", url);

        MvcResult resultShort = mockMvc.perform(post(ControllerPath.ADMINISTRATION_SHORT)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestMapShort)))
                .andDo(print())
                .andReturn();

        String contentShort = resultShort.getResponse().getContentAsString();
        System.out.println("CONTENT SHORT = " + contentShort);
        String shortUrl = ResponseReader.getShortUrlFromShortingResponse(contentShort);
        System.out.println("SHORT URL = " + shortUrl);

        ResultActions response = mockMvc.perform(get(ControllerPath.REDIRECTION)
                .contentType(MediaType.APPLICATION_JSON)
                .param("shortUrl", shortUrl));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.url", CoreMatchers.is(url)))
                .andDo(print());
    }
}
