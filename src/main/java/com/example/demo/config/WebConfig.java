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
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedHeaders("*");
            }

            @Override
            public void addResourceHandlers(
                    @NonNull org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry registry) {
                java.nio.file.Path uploadDir = java.nio.file.Paths.get("uploads");
                String uploadPath = uploadDir.toFile().getAbsolutePath();

                // สำหรับ Windows ต้องใช้ file:/// เพื่อความถูกต้อง
                String resourceLocation = "file:///" + uploadPath.replace("\\", "/") + "/";

                registry.addResourceHandler("/uploads/**")
                        .addResourceLocations(resourceLocation);
            }
        };
    }

}