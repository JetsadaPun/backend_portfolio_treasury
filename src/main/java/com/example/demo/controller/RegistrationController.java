package com.example.demo.controller;

import com.example.demo.dto.RegisterStartRequest;
import com.example.demo.dto.VerifyCodeRequest;
import com.example.demo.service.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/register")
@CrossOrigin(origins = "*")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    // ขั้นตอนที่ 1: สมัคร → ส่งโค้ดไปอีเมล (ยังไม่สร้าง users)
    @PostMapping("/start")
    public ResponseEntity<Map<String, Object>> startRegister(@Valid @RequestBody RegisterStartRequest req) {
        Map<String, Object> res = registrationService.startRegister(req);
        return res.getOrDefault("success", false).equals(Boolean.TRUE)
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }

    // ขั้นตอนที่ 2: ผู้ใช้ส่ง code กลับมายืนยัน → ค่อยสร้าง users จริง
    @PostMapping("/verify")
    public ResponseEntity<Map<String, Object>> verify(@Valid @RequestBody VerifyCodeRequest req) {
        Map<String, Object> res = registrationService.verifyAndCreateUser(req);
        return res.getOrDefault("success", false).equals(Boolean.TRUE)
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }
}
