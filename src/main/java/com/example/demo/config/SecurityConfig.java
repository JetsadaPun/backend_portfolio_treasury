package com.example.demo.config;

import com.example.demo.security.JwtFilter;
import com.example.demo.security.OauthLoginSuccessHandler;
import com.example.demo.security.UnifiedOauth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
// PasswordEncoder removed
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        private final UnifiedOauth2UserService oauthUserService;
        private final OauthLoginSuccessHandler oauthSuccessHandler;
        private final JwtFilter jwtFilter;

        public SecurityConfig(JwtFilter jwtFilter,
                        UnifiedOauth2UserService oauthUserService,
                        OauthLoginSuccessHandler oauthSuccessHandler) {
                this.jwtFilter = jwtFilter;
                this.oauthUserService = oauthUserService;
                this.oauthSuccessHandler = oauthSuccessHandler;
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf.disable())
                                .sessionManagement(
                                                sess -> sess.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                                // .sessionManagement(session ->
                                // session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                // .authorizeHttpRequests(authz -> authz
                                // .anyRequest().permitAll() // เอา .authenticated() ออก

                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(
                                                                "/api/register/start",
                                                                "/api/register/verify",
                                                                "/api/users/register",
                                                                "/api/users/login",
                                                                "/api/subjects",
                                                                "/api/subjects/**",
                                                                "/api/posts/**",
                                                                "/oauth-success",
                                                                "/oauth2/authorization/**",
                                                                "/login/oauth2/**",
                                                                "/error",
                                                                "/public/**",
                                                                "/actuator/health")
                                                .permitAll()
                                                .requestMatchers(HttpMethod.POST, "/api/subjects/import-csv")
                                                .hasRole("ADMIN")
                                                .anyRequest().authenticated())

                                .oauth2Login(oauth -> oauth
                                                // เริ่มต้นด้วย /oauth2/authorization/{registrationId}
                                                .userInfoEndpoint(userInfo -> userInfo.userService(oauthUserService))
                                                .successHandler(oauthSuccessHandler));

                // เพิ่ม JWT filter ข้างหน้า UsernamePasswordAuthenticationFilter
                http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
                return http.build();
        }
}
