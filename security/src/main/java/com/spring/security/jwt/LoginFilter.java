package com.spring.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Map;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public LoginFilter(AuthenticationManager authenticationManager) {

        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl("/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        ObjectMapper objectMapper = new ObjectMapper();
        String email = null;
        String password = null;

        try {
            // JSON 형식의 요청 본문에서 email과 password를 추출
            Map<String, String> requestBody = objectMapper.readValue(request.getInputStream(), Map.class);
            email = requestBody.get("email");
            password = requestBody.get("password");

            /**
             * multipart/form-data 형식의 데이터를 처리할 땐 아래왕 같이 처리.
             * String email = request.getParameter("email");
             * String password = request.getParameter("password");
             */

        } catch (IOException e) {
            throw new AuthenticationServiceException("Request parsing failed", e);
        }

        System.out.println("Email: " + email);
        System.out.println("Password: " + password);

        // Spring Security에서 email과 password를 검증하기 위해 token에 담아야 함.
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, null);

        // Token에 담은 검증을 위한 AuthenticationManager로 전달.
        return authenticationManager.authenticate(authToken);
    }

    // 로그인 성공 시 실행하는 메서드 (여기서 JWT Token을 발급.)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {

        System.out.println("Success");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {

        System.out.println("Fail");
    }
}
