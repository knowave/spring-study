package com.spring.security.config;

import com.spring.security.jwt.JwtFilter;
import com.spring.security.jwt.JwtProvider;
import com.spring.security.jwt.LoginFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collection;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtProvider jwtProvider;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JwtProvider jwtProvider) {

        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtProvider = jwtProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // CORS 설정
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        // csrf disable
        http
                .csrf(auth -> auth.disable());

        // From 로그인 방식 disable
        http
                .formLogin(auth -> auth.disable());

        // HTTP basic 인증 방식 disable
        http
                .httpBasic(auth -> auth.disable());

        // 경로별 인가 작업
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/", "/join").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .anyRequest().authenticated());

        // JWT Filter 등록
        http
                .addFilterBefore(new JwtFilter(jwtProvider), LoginFilter.class);

        // login filter 등록
        http
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtProvider), UsernamePasswordAuthenticationFilter.class);

        // 세션 설정 (JWT 방식에서는 Session을 statusLess로 관리함)
        http
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    private CorsConfigurationSource corsConfigurationSource() {
        return new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                CorsConfiguration configuration = new CorsConfiguration();

                configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                configuration.setAllowedMethods(Collections.singletonList("*"));
                configuration.setAllowedHeaders(Collections.singletonList("*"));
                configuration.setAllowCredentials(true);
                configuration.setMaxAge(3600L);

                configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                return configuration;
            }
        };
    }
}