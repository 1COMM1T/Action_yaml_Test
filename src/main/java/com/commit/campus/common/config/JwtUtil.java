package com.commit.campus.common.config;

import com.commit.campus.service.CognitoUserService;
import com.commit.campus.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.security.Key;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class JwtUtil {

    private final Key hs512Key;
    private final long accessTokenExpTime;
    private final UserService userService;
    private final PublicKey rsaPublicKey;
    private final CognitoUserService cognitoUserService;
    private static final String ALGORITHM_RS256 = "RS256";
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;


    public JwtUtil(
            @Value("${token.secret}") String hs512secretKey,
            @Value("${token.expiration_time}") long accessTokenExpTime,
            @Value("${spring.security.oauth2.client.provider.cognito.jwk-set-uri}") String jwksUrl,
            UserService userService,
            CognitoUserService cognitoUserService, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.cognitoUserService = cognitoUserService;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        byte[] decodedKey = Base64.getUrlDecoder().decode(hs512secretKey);
        this.hs512Key = Keys.hmacShaKeyFor(decodedKey);
        this.accessTokenExpTime = accessTokenExpTime;
        this.userService = userService;
        this.rsaPublicKey = getRsaPublicKey(jwksUrl);
    }

    private PublicKey getRsaPublicKey(String jwksUrl) {
        try {
            String jwks = restTemplate.getForObject(jwksUrl, String.class);
            JsonNode jwksNode = objectMapper.readTree(jwks);
            JsonNode keys = jwksNode.get("keys");

            JsonNode keyNode = keys.get(0);
            String modulus = keyNode.get("n").asText();
            String exponent = keyNode.get("e").asText();

            byte[] modulusBytes = Base64.getUrlDecoder().decode(modulus);
            byte[] exponentBytes = Base64.getUrlDecoder().decode(exponent);

            RSAPublicKeySpec spec = new RSAPublicKeySpec(new java.math.BigInteger(1, modulusBytes), new java.math.BigInteger(1, exponentBytes));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(spec);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate public key from jwks", e);
        }
    }

    public Authentication getAuthentication(String token) {
        Jws<Claims> jwsClaims = parseToken(token);
        Claims claims = jwsClaims.getBody();
        String algorithm = jwsClaims.getHeader().getAlgorithm();

        UserDetails userDetails;
        if (ALGORITHM_RS256.equals(algorithm)) {
            // RSA 서명 알고리즘인 경우 CognitoUserService로 보냄
            userDetails = cognitoUserService.loadUserByUsername(this.getUserId(claims));
        } else {
            // 그렇지 않은 경우 UserService로 보냄
            userDetails = userService.loadUserByUsername(this.getUserId(claims));
        }

        if (claims.get("role") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        String role = claims.get("role").toString();
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role));

        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = parseToken(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token {}", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token {}", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token {}", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty {}", e);
        }
        return false;
    }

    private Jws<Claims> parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(rsaPublicKey)  // RSA 공개 키를 사용
                    .build()
                    .parseClaimsJws(token);
        } catch (JwtException e) {
            // JWT가 RSA로 서명되지 않은 경우 HS512 키를 사용
            return Jwts.parserBuilder()
                    .setSigningKey(hs512Key)  // HS512 비밀 키를 사용
                    .build()
                    .parseClaimsJws(token);
        }
    }

    private String getUserId(Claims claims) {
        return claims.getSubject();
    }
}
