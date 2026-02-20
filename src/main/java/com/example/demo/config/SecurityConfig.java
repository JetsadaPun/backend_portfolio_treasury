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
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
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
                                                                "/api/users/search",
                                                                "/api/users/profile/**",
                                                                "/api/users/*",
                                                                "/api/subjects",
                                                                "/api/subjects/**",
                                                                "/api/posts/**",
                                                                "/api/actions/**",
                                                                "/oauth-success",
                                                                "/oauth2/authorization/**",
                                                                "/login/oauth2/**",
                                                                "/error",
                                                                "/public/**",
                                                                "/uploads/**",
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

        @Bean
        public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
                org.springframework.web.cors.CorsConfiguration configuration = new org.springframework.web.cors.CorsConfiguration();
                configuration.setAllowedOrigins(java.util.List.of("http://localhost:3000"));
                configuration.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(java.util.List.of("Authorization", "Content-Type", "X-Requested-With"));
                configuration.setAllowCredentials(true);
                org.springframework.web.cors.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }
}
