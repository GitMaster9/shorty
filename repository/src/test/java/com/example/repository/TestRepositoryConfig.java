package com.example.repository;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootConfiguration
@Import({ConfigurationRepository.class})
public class TestRepositoryConfig {
}