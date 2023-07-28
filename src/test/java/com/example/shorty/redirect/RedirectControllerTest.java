package com.example.shorty.redirect;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(controllers = RedirectController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class RedirectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RedirectService redirectService;

    @Test
    void redirectURL() throws Exception {
        String shortUrl = "abcdefg";
        String url = "www.google.com";
        int redirectType = 302;

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("url", url);
        responseMap.put("redirectType", redirectType);
        ResponseEntity<Object> responseEntity = new ResponseEntity<>(responseMap, HttpStatus.OK);

        given(redirectService.getUrl(shortUrl)).willReturn(responseEntity);

        ResultActions response = mockMvc.perform(get("/redirection/get")
                .contentType(MediaType.APPLICATION_JSON)
                .param("shortUrl", shortUrl));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }
}