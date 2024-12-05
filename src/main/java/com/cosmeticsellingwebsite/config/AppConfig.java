package com.cosmeticsellingwebsite.config;

import com.cosmeticsellingwebsite.util.DateTimeUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public DateTimeUtils dateTimeUtils() {
        return new DateTimeUtils();
    }
}
