//package com.spring.security.jwt;
//
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import javax.crypto.SecretKey;
//import java.util.Date;
//
//@Slf4j
//@Component
//public class JwtProvider {
//
//    private final SecretKey key;
//
//    @Value("${jwt.token-validity}")
//    private long tokenValidity;
//
//    public JwtProvider(@Value("${jwt.secret}") String secret) {
//        byte[] keyBytes = Decoders.BASE64.decode(secret);
//        this.key = Keys.hmacShaKeyFor(keyBytes);
//    }
//
//    public String generateToken(String username) {
//        return Jwts.builder()
//                .setSubject(username)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + tokenValidity))
//                .signWith(key, SignatureAlgorithm.HS512)
//                .compact();
//    }
//
//    public String getUsernameFromToken(String token) {
//        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
//        return claims.getSubject();
//    }
//
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
//            return true;
//        } catch (JwtException | IllegalArgumentException e) {
//            log.error("Invalid JWT token.");
//        }
//        return false;
//    }
//}
