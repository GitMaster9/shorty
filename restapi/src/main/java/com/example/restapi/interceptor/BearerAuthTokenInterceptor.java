package com.example.restapi.interceptor;

import com.example.restapi.exception.ApiBadRequestException;
import com.example.restapi.exception.ExceptionMessages;
import com.example.core.utils.TokenEncoder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;

public class BearerAuthTokenInterceptor implements HandlerInterceptor {
    public final Logger logger = LogManager.getLogger(this);

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler) {

        final String authHeader = request.getHeader("Authorization");

        if (TokenEncoder.isBearerTokenInvalid(authHeader)) {
            logger.info("BAD REQUEST - " + ExceptionMessages.BAD_BEARER_TOKEN);
            throw new ApiBadRequestException(ExceptionMessages.BAD_BEARER_TOKEN);
        }

        return true;
    }
}