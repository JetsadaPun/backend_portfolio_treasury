package com.example.demo.service;

import com.example.demo.dto.RegisterStartRequest;
import com.example.demo.dto.VerifyCodeRequest;
import com.example.demo.model.User;
import com.example.demo.model.VerifyUser;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.VerifyUserRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;

@Service
public class RegistrationService {
    @Value("${app.frontend.base-url}")
    private String frontendBaseUrl;
    private final UserRepository userRepository;
    private final VerifyUserRepository verifyUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    public RegistrationService(UserRepository userRepository,
            VerifyUserRepository verifyUserRepository,
            PasswordEncoder passwordEncoder,
            JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.verifyUserRepository = verifyUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    // สร้างโค้ด 6 หลักแบบปลอดภัย
    private String generateCode() {
        SecureRandom rnd = new SecureRandom();
        int n = 100000 + rnd.nextInt(900000); // 6 digits
        return String.valueOf(n);
    }

    @Transactional
    public Map<String, Object> startRegister(RegisterStartRequest req) {
        final String email = req.email().trim().toLowerCase();

        // ถ้า email ถูกใช้แล้วใน users → ไม่อนุญาตสมัครใหม่
        if (userRepository.existsByEmail(email)) {
            return Map.of("success", false, "error", "Email is already registered");
        }

        // ถ้าเคยมีคำขอ PENDING ค้างอยู่ → จะ reuse หรือสร้างใหม่ก็ได้
        // ค้นหาว่ามีขออยู่ไหม
        var existingOpt = verifyUserRepository.findTopByEmailIgnoreCaseAndStatusOrderByRequestedAtDesc(email,
                "PENDING");
        VerifyUser v;
        if (existingOpt.isPresent()) {
            v = existingOpt.get();
        } else {
            v = new VerifyUser();
            v.setEmail(email);
        }

        String code = generateCode();
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        OffsetDateTime expiresAt = now.plusMinutes(15);

        v.setName(req.name().trim());
        v.setPasswordHash(passwordEncoder.encode(req.password())); // เก็บเป็น hash
        v.setVerificationCode(code);
        v.setStatus("PENDING");
        v.setRequestedAt(now);
        v.setExpiresAt(expiresAt);
        v.setAttempts(0);

        verifyUserRepository.save(v);

        // ส่งอีเมลโค้ด
        try {
            var mime = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(mime, "UTF-8");
            helper.setTo(email);
            helper.setSubject("Your verification code");

            String html = """
                    <!doctype html>
                    <html>
                      <body style="font-family:system-ui,Segoe UI,Roboto,Arial">
                        <div style="max-width:560px;margin:24px auto;padding:24px;border:1px solid #eee;border-radius:12px">
                          <h2 style="margin:0 0 12px;color:#111">ยืนยันอีเมลของคุณ</h2>
                          <p>สวัสดี <b>%s</b>,</p>
                          <p>รหัสยืนยันของคุณคือ:</p>
                          <div style="font-size:28px;letter-spacing:4px;font-weight:700;margin:12px 0">%s</div>
                          <p style="color:#666">รหัสนี้จะหมดอายุใน <b>15 นาที</b></p>
                          <hr style="margin:24px 0;border:none;border-top:1px solid #eee">
                          <p style="color:#999;font-size:12px">หากคุณไม่ได้ร้องขอการสมัครนี้ โปรดละเว้นอีเมลนี้</p>
                        </div>
                      </body>
                    </html>
                    """
                    .formatted(req.name(), code);
            helper.setText(html, true);
            mailSender.send(mime);
        } catch (MessagingException e) {
            throw new RuntimeException("ส่งอีเมลไม่สำเร็จ", e);
        }

        return Map.of(
                "success", true,
                "message", "Verification code sent to email");
    }

    @Transactional
    public Map<String, Object> verifyAndCreateUser(VerifyCodeRequest req) {
        final String email = req.email().trim().toLowerCase();

        // หา PENDING ล่าสุดของอีเมลนี้
        var pendingOpt = verifyUserRepository.findTopByEmailIgnoreCaseAndStatusOrderByRequestedAtDesc(email, "PENDING");
        if (pendingOpt.isEmpty()) {
            return Map.of("success", false, "error", "No pending verification found");
        }
        VerifyUser v = pendingOpt.get();

        // หมดอายุ?
        if (OffsetDateTime.now(ZoneOffset.UTC).isAfter(v.getExpiresAt())) {
            v.setStatus("EXPIRED");
            verifyUserRepository.save(v);
            return Map.of("success", false, "error", "Verification code expired");
        }

        // ตรวจโค้ด
        if (!v.getVerificationCode().equals(req.code().trim())) {
            v.setAttempts(v.getAttempts() + 1);
            verifyUserRepository.save(v);
            return Map.of("success", false, "error", "Invalid verification code");
        }

        // โค้ดถูกต้อง → อัปเดตสถานะ แล้วสร้าง User จริง
        v.setStatus("VERIFIED");
        v.setVerifiedAt(OffsetDateTime.now(ZoneOffset.UTC));
        verifyUserRepository.save(v);

        // กันซ้ำเชิง race: เช็คอีกครั้งว่า users ยังไม่มีอีเมลนี้
        if (userRepository.existsByEmail(email)) {
            return Map.of("success", true, "message", "Already verified and account exists");
        }

        User u = new User();
        u.setName(v.getName());
        u.setEmail(email);
        u.setPassword(v.getPasswordHash()); // ใช้ hash ที่เก็บไว้ (อย่านำ plaintext มาเก็บ)
        u.setRole("USER");
        u.setStatus("active");
        userRepository.save(u);

        return Map.of("success", true, "message", "Account created successfully after verification");
    }
}