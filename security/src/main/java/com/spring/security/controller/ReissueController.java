package com.spring.security.controller;

import com.spring.security.entity.Refresh;
import com.spring.security.jwt.JwtProvider;
import com.spring.security.repository.RefreshRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@ResponseBody
public class ReissueController {

    private final JwtProvider jwtProvider;
    private final RefreshRepository refreshRepository;

    @Value("${jwt.access-token-expiration}")
    private final Long accessTokenExpirationMs;

    @Value("${jwt.refresh-token-expiration}")
    private final Long refreshTokenExpirationMs;

    public ReissueController(JwtProvider jwtProvider, RefreshRepository refreshRepository, Long accessTokenExpirationMs, Long refreshTokenExpirationMs) {

        this.jwtProvider = jwtProvider;
        this.refreshRepository = refreshRepository;
        this.accessTokenExpirationMs = accessTokenExpirationMs;
        this.refreshTokenExpirationMs = refreshTokenExpirationMs;
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        // get refresh token
        String refresh = null;
        Cookie[] cookies = request.getCookies();

        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("refresh")) {

                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {

            // response status code
            return new ResponseEntity<>("refresh token is null", HttpStatus.BAD_REQUEST);
        }

        // expired check
        try {
            jwtProvider.isExpired(refresh);
        } catch (ExpiredJwtException e) {

            // response status code
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        // 토큰이 refresh인지 확인 (발급 시 페이로드에 명시)
        String category = jwtProvider.getCategory(refresh);

        if (!category.equals("refresh")) {

            // response status code
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        String email = jwtProvider.getEmail(refresh);
        String role = jwtProvider.getRole(refresh);

        // make new jwt
        String newAccessToken = jwtProvider.createToken(category, email, role, accessTokenExpirationMs);
        String newRefreshToken = jwtProvider.createToken(category, email, role, refreshTokenExpirationMs);

        refreshRepository.deleteByRefresh(refresh);
        createRefresh(email, newRefreshToken, 86400000L);

        // response
        response.setHeader("access", newAccessToken);
        response.addCookie(createCookie("refresh", newRefreshToken));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true);
        //cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

    private void createRefresh(String email, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        Refresh refreshEntity = Refresh.builder()
                .email(email)
                .refresh(refresh)
                .expiration(date.toString())
                .build();

        refreshRepository.save(refreshEntity);
    }
}
