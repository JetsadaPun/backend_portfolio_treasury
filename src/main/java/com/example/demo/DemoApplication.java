package com.example.demo;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        // โหลด .env และ set เข้า System Properties
        Dotenv dotenv = Dotenv.load();
        dotenv.entries().forEach(entry -> {
            System.setProperty(entry.getKey(), entry.getValue());
        });

        // เริ่ม Spring Boot
        SpringApplication.run(DemoApplication.class, args);
    }
}
