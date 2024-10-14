package com.spring.security.jwt;

import com.spring.security.dto.CustomUserDetails;
import com.spring.security.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    public JwtFilter(JwtProvider jwtProvider) {

        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // request에서 Authorization 헤더 찾기
        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer")) {

            System.out.println("Token Null");
            filterChain.doFilter(request, response);

            // 조건에 해당되면 메서드 종료 (필수)
            return;
        }

        System.out.println("authorization now");

        String token = authorization.split(" ")[1];

        if (jwtProvider.isExpired(token)) {

            System.out.println("token expired");
            filterChain.doFilter(request, response);

            // 조건에 해당되면 메서드 종료 (필수)
            return;
        }

        // Token에서 email과 role을 가져옴.
        String email = jwtProvider.getEmail(token);
        String role = jwtProvider.getRole(token);

        // user를 생성하여 값 set
        User user = User.builder()
                .email(email)
                .username("username")
                .password("tempPasswor")
                .role(role)
                .build();

        // UserDetails 회원 정보 객체 담기
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        // 스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        // 세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
