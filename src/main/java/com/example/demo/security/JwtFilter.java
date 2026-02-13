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
        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpRes = (HttpServletResponse) response;

        String authHeader = httpReq.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                // ตรวจสอบ token และดึง username
                String email = jwtUtil.extractUsername(token);

                if (email != null && org.springframework.security.core.context.SecurityContextHolder.getContext()
                        .getAuthentication() == null) {

                    // Extract role from token
                    String role = jwtUtil.extractClaim(token, claims -> claims.get("role", String.class));
                    if (role == null)
                        role = "USER"; // Default

                    java.util.List<org.springframework.security.core.GrantedAuthority> authorities = java.util.List
                            .of(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + role));

                    // สร้าง authentication object และเซ็ตลงใน SecurityContext
                    org.springframework.security.authentication.UsernamePasswordAuthenticationToken authToken = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                            email, null, authorities);

                    authToken.setDetails(
                            new org.springframework.security.web.authentication.WebAuthenticationDetailsSource()
                                    .buildDetails(httpReq));
                    org.springframework.security.core.context.SecurityContextHolder.getContext()
                            .setAuthentication(authToken);
                }
            } catch (Exception e) {
                // token ไม่ถูกต้องหรือหมดอายุ
                httpRes.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpRes.setContentType("application/json");
                httpRes.getWriter().write("{\"error\":\"Invalid or expired token\"}");
                httpRes.getWriter().flush();
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
