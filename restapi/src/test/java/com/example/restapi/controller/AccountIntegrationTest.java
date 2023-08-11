package com.example.restapi.controller;

import com.example.core.ControllerPath;
import com.example.core.utils.ResponseReader;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = ShortyApplication.class
)
@TestPropertySource(locations = "classpath:application-test.properties")
public class AccountIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void registerTestBadRequest() throws Exception {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("account", "karlo1");

        ResultActions response = mockMvc.perform(post(ControllerPath.ADMINISTRATION_REGISTER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestMap)));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(ExceptionMessages.MISSING_ACCOUNT_ID)))
                .andDo(print());
    }

    @Test
    public void registerExistingUserTest() throws Exception {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("accountId", "karlo2");

        ResultActions response1 = mockMvc.perform(post(ControllerPath.ADMINISTRATION_REGISTER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestMap)));

        response1.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", CoreMatchers.is(true)))
                .andDo(print());

        ResultActions response2 = mockMvc.perform(post(ControllerPath.ADMINISTRATION_REGISTER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestMap)));

        response2.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", CoreMatchers.is(false)))
                .andDo(print());
    }

    @Test
    public void loginTestFail() throws Exception {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("accountId", "karlo3");
        requestMap.put("password", "password");

        ResultActions response = mockMvc.perform(post(ControllerPath.ADMINISTRATION_LOGIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestMap)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", CoreMatchers.is(false)))
                .andDo(print());
    }

    @Test
    public void loginTestSuccess() throws Exception {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("accountId", "karlo4");

        MvcResult result = mockMvc.perform(post(ControllerPath.ADMINISTRATION_REGISTER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestMap))).andReturn();

        String content = result.getResponse().getContentAsString();
        String password = ResponseReader.getPasswordFromRegisterResponse(content);

        requestMap.put("password", password);

        ResultActions response = mockMvc.perform(post(ControllerPath.ADMINISTRATION_LOGIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestMap)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", CoreMatchers.is(true)))
                .andDo(print());
    }
}
