package com.spring.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Bean
    public Long accessTokenExpirationMs(@Value("${jwt.access-token-expiration}") Long accessTokenExpirationMs) {
        return accessTokenExpirationMs;
    }

    @Bean
    public Long refreshTokenExpirationMs(@Value("${jwt.refresh-token-expiration}") Long refreshTokenExpirationMs) {
        return refreshTokenExpirationMs;
    }
}
