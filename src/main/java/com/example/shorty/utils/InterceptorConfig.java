package com.example.shorty.utils;

import com.example.shorty.repository.AccountRepository;
import com.example.shorty.restapi.ControllerPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new BasicAuthHandlerInterceptor(accountRepository)).addPathPatterns(ControllerPath.ADMINISTRATION_STATISTICS, ControllerPath.ADMINISTRATION_SHORT);
    }
}
