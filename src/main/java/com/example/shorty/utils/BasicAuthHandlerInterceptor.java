package com.example.shorty.utils;

import com.example.shorty.exception.ExceptionMessages;
import com.example.shorty.repository.AccountRepository;
import com.example.shorty.restapi.Account;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class BasicAuthHandlerInterceptor implements HandlerInterceptor {

    private final AccountRepository accountRepository;

    public BasicAuthHandlerInterceptor(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler) throws Exception {

        String authHeader = request.getHeader("Authorization");

        Account found = authenticateAccount(authHeader);
        if (found == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ExceptionMessages.UNAUTHORIZED);
            return false;
        }

        return true;
    }

    private Account authenticateAccount(String token) {
        if (!token.startsWith(TokenEncoder.BASIC_TOKEN_START)) return null;

        final String[] decodedStrings = TokenEncoder.decodeBasicToken(token);
        final String accountId = decodedStrings[0];
        final String password = decodedStrings[1];

        return accountRepository.findByAccountIdAndPassword(accountId, password);
    }
}
