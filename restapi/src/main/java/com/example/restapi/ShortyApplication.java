package com.example.restapi;

import com.example.repository.ConfigurationRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({ConfigurationRepository.class})
public class ShortyApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(ShortyApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ShortyApplication.class);
	}
}
