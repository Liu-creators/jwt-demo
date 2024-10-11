package com.example.jwtdemo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    // 刷新令牌的密钥和过期时间
    @Value("${jwt.refresh.secret}")
    private String refreshSecret;

    @Value("${jwt.refresh.expiration}")
    private Long refreshExpirationTime;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createAccessToken(claims, userDetails.getUsername());
    }

    private String createAccessToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


    // 新增的刷新令牌相关方法
    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createRefreshToken(claims, userDetails.getUsername());
    }

    private String createRefreshToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpirationTime * 1000))
                .signWith(SignatureAlgorithm.HS256, refreshSecret).compact();
    }

    public Boolean validateRefreshToken(String token) {
        try {
            Jwts.parser().setSigningKey(refreshSecret).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String extractUsernameFromRefreshToken(String token) {
        return extractClaimFromRefreshToken(token, Claims::getSubject);
    }

    private <T> T extractClaimFromRefreshToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaimsFromRefreshToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaimsFromRefreshToken(String token) {
        return Jwts.parser().setSigningKey(refreshSecret).parseClaimsJws(token).getBody();
    }

    public String generateTokenFromRefreshToken(String refreshToken) {
        String username = extractUsernameFromRefreshToken(refreshToken);
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }
}