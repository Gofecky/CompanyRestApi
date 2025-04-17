package pl.kurs.companyrestapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    ObjectMapper getMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule());
    }
}