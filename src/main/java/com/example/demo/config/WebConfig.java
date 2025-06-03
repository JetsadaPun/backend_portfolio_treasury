package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.lang.NonNull;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/api/**") // อนุญาต CORS สำหรับทุก endpoint ที่ขึ้นต้นด้วย /api/
                        .allowedOrigins("http://localhost:3000") // อนุญาต origin นี้เท่านั้น
                        .allowedMethods("GET", "POST", "PUT", "DELETE") // อนุญาต method ที่ต้องการ
                        .allowedHeaders("*"); // อนุญาตทุก header
            }
        };
    }
}