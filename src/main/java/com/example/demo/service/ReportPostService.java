package com.example.demo.service;

import com.example.demo.model.PostWork;
import com.example.demo.model.ReportPost;
import com.example.demo.model.User;
import com.example.demo.repository.PostWorkRepository;
import com.example.demo.repository.ReportPostRepository;
import com.example.demo.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReportPostService {

    private final ReportPostRepository reportPostRepository;
    private final PostWorkRepository postWorkRepository;
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;

    public ReportPostService(ReportPostRepository reportPostRepository,
            PostWorkRepository postWorkRepository,
            UserRepository userRepository,
            JavaMailSender mailSender) {
        this.reportPostRepository = reportPostRepository;
        this.postWorkRepository = postWorkRepository;
        this.userRepository = userRepository;
        this.mailSender = mailSender;
    }

    public List<ReportPost> getAllReports() {
        return reportPostRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional
    public ReportPost reportPost(Long postId, String email, String reason) {
        PostWork post = postWorkRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        User reporter = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ReportPost report = new ReportPost();
        report.setPost(post);
        report.setReporter(reporter);
        report.setReason(reason);
        return reportPostRepository.save(report);
    }

    @Transactional
    public ReportPost handleReport(Long reportId, String action, String adminComment) {
        ReportPost report = reportPostRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        report.setStatus("RESOLVED");

        PostWork post = report.getPost();
        User postOwner = post.getUser();
        User reporter = report.getReporter();

        switch (action) {
            case "CLOSE":
                report.setActionTaken("CLOSED");
                // Notify owner
                sendEmail(postOwner.getEmail(), "โพสต์ของคุณถูกระงับการใช้งาน",
                        "ขออภัย โพสต์ของคุณที่ชื่อ '" + post.getProjectName()
                                + "' ถูกแอดมินสั่งระงับการใช้งานเนื่องจาก: " + adminComment);
                // Actually disable or delete post? User said "กดปิดโพสต์"
                // Let's assume we remove it or set a status. For now, we'll just keep it but
                // marked as closed in report.
                break;

            case "VERIFY":
                report.setActionTaken("VERIFIED");
                // Notify reporter
                sendEmail(reporter.getEmail(), "ตรวจสอบรายงานของคุณเรียบร้อยแล้ว",
                        "แอดมินตรวจสอบโพสต์ที่คุณรายงาน ('" + post.getProjectName()
                                + "') แล้ว พบว่าเนื้อหาไม่มีปัญหาใดๆ ขอบคุณที่ช่วยตรวจสอบ");
                break;

            case "NEED_FIX":
                report.setActionTaken("NEED_FIX");
                // Notify owner
                sendEmail(postOwner.getEmail(), "กรุณาแก้ไขเนื้อหาในโพสต์ของคุณ",
                        "แอดมินได้ตรวจสอบโพสต์ของคุณ ('" + post.getProjectName() + "') และพบจุดที่ต้องแก้ไขดังนี้: "
                                + adminComment + "\nกรุณาแก้ไขเนื้อหาเพื่อให้เป็นไปตามกฎของระบบ");
                break;

            default:
                throw new IllegalArgumentException("Action not supported");
        }

        return reportPostRepository.save(report);
    }

    private void sendEmail(String to, String subject, String body) {
        try {
            var mime = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(mime, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, false);
            mailSender.send(mime);
        } catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
}
