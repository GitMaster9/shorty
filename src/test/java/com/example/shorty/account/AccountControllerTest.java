package com.example.shorty.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    void registerNewAccountTest() throws Exception {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("accountId", "karlo");

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("success", true);
        responseMap.put("password", "dummypwd");

        ResponseEntity<Object> responseEntity = new ResponseEntity<>(responseMap, HttpStatus.OK);

        given(accountService.addNewAccount(requestMap)).willReturn(responseEntity);

        ResultActions response = mockMvc.perform(post("/administration/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestMap)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", CoreMatchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password", CoreMatchers.is("dummypwd")))
                .andDo(print());
    }

    @Test
    void loginAccountTest() throws Exception {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("accountId", "karlo");
        requestMap.put("password", "dummypwd");

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("success", true);

        ResponseEntity<Object> responseEntity = new ResponseEntity<>(responseMap, HttpStatus.OK);

        given(accountService.loginAccount(requestMap)).willReturn(responseEntity);

        ResultActions response = mockMvc.perform(post("/administration/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestMap)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", CoreMatchers.is(true)))
                .andDo(print());
    }
}