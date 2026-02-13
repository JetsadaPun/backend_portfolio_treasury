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

    @org.springframework.context.annotation.Bean
    org.springframework.boot.CommandLineRunner seedData(
            com.example.demo.repository.SubjectRepository subjectRepository) {
        return args -> {
            if (subjectRepository.count() == 0) {
                com.example.demo.model.Subject s = new com.example.demo.model.Subject();
                s.setSubjectNameId("01418496");
                s.setSubjectNameTh("เรื่องเฉพาะทางวิทยาการคอมพิวเตอร์");
                s.setSubjectNameEn("Selected Topic in Computer Science");
                s.setDescription("Selected Topic in Computer Science");
                subjectRepository.save(s);
                System.out.println("SEED: Added default subject 01418496");
            }
        };
    }
}
