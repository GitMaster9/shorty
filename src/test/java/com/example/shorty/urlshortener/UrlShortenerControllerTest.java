package com.example.shorty.urlshortener;

import com.example.shorty.token.TokenEncoder;
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
import static org.mockito.BDDMockito.given;
import java.util.HashMap;
import java.util.Map;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(controllers = UrlShortenerController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class UrlShortenerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UrlShortenerService urlShortenerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getShortURLTest() throws Exception {
        String accountId = "karlo";
        String password = "password";
        String token = TokenEncoder.encodeBasicToken(accountId, password);

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("url", "www.google.com");

        Map<String, Object> responseMap = new HashMap<>();
        String shortUrl = "www.shorty.com/fake";
        responseMap.put("shortUrl", shortUrl);

        ResponseEntity<Object> responseEntity = new ResponseEntity<>(responseMap, HttpStatus.OK);

        given(urlShortenerService.getShortURL(token, requestMap)).willReturn(responseEntity);

        ResultActions response = mockMvc.perform(post("/administration/short")
                        .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestMap)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.shortUrl", CoreMatchers.is(shortUrl)))
                .andDo(print());
    }

    @Test
    void getUserStatisticsTest() throws Exception {
        String accountId = "karlo";
        String password = "password";
        String token = TokenEncoder.encodeBasicToken(accountId, password);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("www.google.com", 10);
        responseMap.put("www.facebook.com", 23);
        responseMap.put("www.youtube.com", 47);

        ResponseEntity<Object> responseEntity = new ResponseEntity<>(responseMap, HttpStatus.OK);

        given(urlShortenerService.getStatistics(token)).willReturn(responseEntity);

        ResultActions response = mockMvc.perform(get("/administration/statistics")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }
}