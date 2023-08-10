package com.example.repository;

import com.example.core.model.Account;
import com.example.core.model.UrlShortener;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
//@EnableJpaRepositories(basePackages = "com.example.repository") // bez ovog ne radi test // sa ovim ne radi aplikacija
@EntityScan(basePackageClasses = {Account.class, UrlShortener.class})
@ComponentScan(basePackageClasses = {AccountRepository.class, UrlShortenerRepository.class})
public class ConfigurationRepository {
}
