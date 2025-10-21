package com.example.demo.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtFilter implements Filter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        chain.doFilter(request, response);
//        HttpServletRequest httpReq = (HttpServletRequest) request;
//        HttpServletResponse httpRes = (HttpServletResponse) response;
//
//        String authHeader = httpReq.getHeader("Authorization");
//
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            String token = authHeader.substring(7);
//
//            try {
//                // ตรวจสอบ token ว่ายัง valid อยู่หรือไม่
//                jwtUtil.extractUsername(token);  // ถ้าไม่ valid จะ throw Exception
//
//                // ถ้า valid, ให้ filter ผ่าน
//                chain.doFilter(request, response);
//            } catch (Exception e) {
//                // token ไม่ถูกต้องหรือหมดอายุ
//                httpRes.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                httpRes.getWriter().write("{\"error\":\"Invalid or expired token\"}");
//                httpRes.getWriter().flush();
//            }
//        } else {
//            // ถ้าไม่มี token หรือ header ไม่ถูกต้อง
//            httpRes.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            httpRes.getWriter().write("{\"error\":\"Authorization header missing or invalid\"}");
//            httpRes.getWriter().flush();
//        }
    }
}
