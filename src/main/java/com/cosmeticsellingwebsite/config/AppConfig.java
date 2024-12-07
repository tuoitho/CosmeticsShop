package com.cosmeticsellingwebsite.config;

import com.cosmeticsellingwebsite.util.DateTimeUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {
    @Bean
    public DateTimeUtils dateTimeUtils() {
        return new DateTimeUtils();
    }

//    @Override
//    public void addFormatters(FormatterRegistry registry) {
//        registry.addFormatter(new LocalDateTimeFormatter());
//    }
}
