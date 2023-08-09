package com.example.shorty.utils;

import com.example.shorty.exception.ApiBadRequestException;
import com.example.shorty.exception.ExceptionMessages;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;

public class BasicAuthTokenInterceptor implements HandlerInterceptor {
    public static final Logger logger = LogManager.getLogger(BasicAuthTokenInterceptor.class);

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler) {

        String authHeader = request.getHeader("Authorization");

        if (!TokenEncoder.isBasicTokenValid(authHeader)) {
            logger.info("BAD REQUEST - " + ExceptionMessages.BAD_TOKEN);
            throw new ApiBadRequestException(ExceptionMessages.BAD_TOKEN);
        }

        return true;
    }
}
