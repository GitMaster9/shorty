package com.example.shorty.utils;

import com.example.shorty.exception.ApiBadRequestException;
import com.example.shorty.exception.ExceptionMessages;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;
import static com.example.shorty.ShortyApplication.logger;

public class BasicAuthTokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler) {

        String authHeader = request.getHeader("Authorization");

        if (!TokenEncoder.isBasicTokenValid(authHeader)) {
            logger.error("Test error by Karlo");
            throw new ApiBadRequestException(ExceptionMessages.BAD_TOKEN);
        }

        return true;
    }
}
