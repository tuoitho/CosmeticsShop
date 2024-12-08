package com.cosmeticsellingwebsite.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary getCloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dd7r5ktuy");
        config.put("api_key", "951415744197275");
        config.put("api_secret", "GYJP1hUf_0wRbUBLUt098zbOTCk");
        config.put("secure", "true");
        return new Cloudinary(config);
    }
}