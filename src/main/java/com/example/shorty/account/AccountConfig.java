package com.example.shorty.account;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class AccountConfig {
    @Bean
    CommandLineRunner commandLineRunner(AccountRepository repository) {
        return args -> {
            Account johnny = new Account("johnny","pass123");
            Account alex = new Account("alex","lozinka");

            repository.saveAll(List.of(johnny, alex));
        };
    }
}
