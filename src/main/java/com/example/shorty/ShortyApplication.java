package com.example.shorty;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class ShortyApplication extends SpringBootServletInitializer {

	public static final Logger logger = LogManager.getLogger("ShortyLogger");

	public static void main(String[] args) {
		SpringApplication.run(ShortyApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ShortyApplication.class);
	}
}
