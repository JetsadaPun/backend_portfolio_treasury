package com.example.demo.config;

import com.example.demo.security.JwtFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@Configuration
public class SecurityConfig {

    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilter(JwtFilter jwtFilter) {
        FilterRegistrationBean<JwtFilter> filter = new FilterRegistrationBean<>();
        filter.setFilter(jwtFilter);
        filter.addUrlPatterns("/api/*"); // ป้องกันทุก endpoint ที่ขึ้นต้นด้วย /api/
        return filter;
    }
}
