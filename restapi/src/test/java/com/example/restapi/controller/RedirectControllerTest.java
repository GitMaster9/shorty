package com.example.restapi.controller;

import com.example.core.ControllerPath;
import com.example.core.model.UrlShortener;
import com.example.restapi.ShortyApplication;
import com.example.restapi.exception.ExceptionMessages;
import com.example.restapi.service.RedirectService;
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
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest(classes = ShortyApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ExtendWith(MockitoExtension.class)
class RedirectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RedirectService redirectService;

    @Test
    void redirectURLTestNotFound() throws Exception {
        String shortUrl = "abcdefg";

        given(redirectService.redirectUrl(shortUrl)).willReturn(null);

        ResultActions response = mockMvc.perform(get(ControllerPath.REDIRECTION)
                .contentType(MediaType.APPLICATION_JSON)
                .param("shortUrl", shortUrl));

        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(ExceptionMessages.URL_NOT_FOUND)))
                .andDo(print());
    }

    @Test
    void redirectURLTestSuccess() throws Exception {
        String shortUrl = "abcdefg";
        String url = "www.google.com";
        int redirectType = 302;

        UrlShortener urlShortener = new UrlShortener();
        urlShortener.setUrl(url);
        urlShortener.setRedirectType(redirectType);

        given(redirectService.redirectUrl(shortUrl)).willReturn(urlShortener);

        ResultActions response = mockMvc.perform(get(ControllerPath.REDIRECTION)
                .contentType(MediaType.APPLICATION_JSON)
                .param("shortUrl", shortUrl));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.url", CoreMatchers.is(url)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.redirectType", CoreMatchers.is(redirectType)))
                .andDo(print());
    }
}