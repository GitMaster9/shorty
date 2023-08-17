package com.example.restapi.controller;

import com.example.core.ControllerPath;
import com.example.core.utils.ResponseReader;
import com.example.core.utils.TokenEncoder;
import com.example.restapi.ShortyApplication;
import com.example.restapi.exception.ExceptionMessages;
import com.example.restapi.security.AccessToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Disabled;
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
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = ShortyApplication.class
)
@TestPropertySource(locations = "classpath:application-test.properties")
public class UrlShortenerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Disabled
    @Test
    void shortURLMissingUrlTest() throws Exception {
        String accountId = "userShortURLMissingUrlTest";

        Map<String, Object> requestMapRegister = new HashMap<>();
        requestMapRegister.put("accountId", accountId);

        MvcResult resultRegister = mockMvc.perform(post(ControllerPath.ADMINISTRATION_REGISTER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestMapRegister))).andReturn();

        String contentRegister = resultRegister.getResponse().getContentAsString();
        String password = ResponseReader.getPasswordFromRegisterResponse(contentRegister);

        String token = AccessToken.getUserToken(accountId, password);

        Map<String, Object> requestMap = new HashMap<>();

        ResultActions response = mockMvc.perform(post(ControllerPath.ADMINISTRATION_SHORT)
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestMap)));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(ExceptionMessages.MISSING_URL)))
                .andDo(print());
    }

    @Disabled
    @Test
    void shortURLSuccessTest() throws Exception {
        String accountId = "userShortURLSuccessTest";
        String url = "https://www.youtube.com/watch?v=rnIeknursww&t=5354s";
        int redirectType = 301;

        Map<String, Object> requestMapRegister = new HashMap<>();
        requestMapRegister.put("accountId", accountId);

        MvcResult resultRegister = mockMvc.perform(post(ControllerPath.ADMINISTRATION_REGISTER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestMapRegister))).andReturn();

        String contentRegister = resultRegister.getResponse().getContentAsString();
        String password = ResponseReader.getPasswordFromRegisterResponse(contentRegister);

        String token = TokenEncoder.getBasicAuthorizationToken(accountId, password);

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("url", url);
        requestMap.put("redirectType", redirectType);

        ResultActions response = mockMvc.perform(post(ControllerPath.ADMINISTRATION_SHORT)
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestMap)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Disabled
    @Test
    void getUserStatisticsSuccessTest() throws Exception {
        String accountId = "userGetUserStatisticsSuccessTest";
        String url = "https://www.youtube.com/watch?v=rnIeknursww&t=8648s";

        Map<String, Object> requestMapRegister = new HashMap<>();
        requestMapRegister.put("accountId", accountId);

        MvcResult resultRegister = mockMvc.perform(post(ControllerPath.ADMINISTRATION_REGISTER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestMapRegister))).andReturn();

        String contentRegister = resultRegister.getResponse().getContentAsString();
        String password = ResponseReader.getPasswordFromRegisterResponse(contentRegister);

        String token = TokenEncoder.getBasicAuthorizationToken(accountId, password);

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("url", url);

        mockMvc.perform(post(ControllerPath.ADMINISTRATION_SHORT)
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestMap)));

        ResultActions response = mockMvc.perform(get(ControllerPath.ADMINISTRATION_STATISTICS)
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(1)))
                .andDo(print());
    }

    @Test
    void unauthorizedTest() throws Exception {
        String accountId = "userUnauthorizedTest";
        String password = "password";
        String token = TokenEncoder.getBasicAuthorizationToken(accountId, password);

        Map<String, Object> dummyRequestMap = new HashMap<>();

        ResultActions responseShorting = mockMvc.perform(post(ControllerPath.ADMINISTRATION_SHORT)
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dummyRequestMap)));

        responseShorting.andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(ExceptionMessages.UNAUTHORIZED)))
                .andDo(print());

        ResultActions responseStatistics = mockMvc.perform(post(ControllerPath.ADMINISTRATION_SHORT)
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dummyRequestMap)));

        responseStatistics.andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(ExceptionMessages.UNAUTHORIZED)))
                .andDo(print());
    }
}
