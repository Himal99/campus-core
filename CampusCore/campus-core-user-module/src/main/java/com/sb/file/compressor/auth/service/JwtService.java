package com.sb.file.compressor.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Himal Rai on 1/14/2024
 * Sb Solutions Nepal pvt.ltd
 * Project sb-back-core.
 */
@Component
@Slf4j
public class JwtService {
    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    private String secret;
    private int jwtExpirationDateIMs;
    private int refreshExpirationDateInMs;


    @Value("${jwt.secret}")
    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Value("${jwt.expirationDateInMs}")
    public void setJwtExpirationDateIMs(int jwtExpirationDateIMs) {
        this.jwtExpirationDateIMs = jwtExpirationDateIMs;
    }

    @Value("${jwt.refreshExpirationDateInMs}")
    public void setRefreshExpirationDateInMs(int refreshExpirationDateInMs) {
        this.refreshExpirationDateInMs = refreshExpirationDateInMs;
    }

    public String generateToken(String userName) {
        Map<String, Object> claims = new HashMap<>();

        return createToken(claims, userName);
    }

    public String generateTokenForApiUser(String userName) {
        Map<String, Object> claims = new HashMap<>();

        return createTokenForApiUser(claims, userName);
    }

    private String createToken(Map<String, Object> claims, String userName) {

        long expirationTimeMillis = System.currentTimeMillis() + (3 * 24 * 60 * 60 * 1000); // for 3 days

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(expirationTimeMillis))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private String createTokenForApiUser(Map<String, Object> claims, String userName) {

        long expirationTimeMillis = System.currentTimeMillis() + (365 * 24 * 60 * 60 * 1000L); //for one year

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(expirationTimeMillis))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extracrAllClaims(token);
        return claimsResolver.apply(claims);
    }


    public Claims extracrAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        boolean isUsernameValid = username.equals(userDetails.getUsername());

        boolean isJwtTokenExpired = isTokenExpired(token);

        return (isUsernameValid && !isJwtTokenExpired);

//        return (isUsernameValid);
    }
}
