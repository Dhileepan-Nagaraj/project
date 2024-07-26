package com.web10.taskmanagement.security;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.web10.taskmanagement.dto.UserPrincipal;
import com.web10.taskmanagement.exceptions.ExpiredTokenException;
import com.web10.taskmanagement.exceptions.InvalidTokenException;
import com.web10.taskmanagement.exceptions.UnsupportedTokenException;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    private Key getKey() {
        return new SecretKeySpec(jwtSecret.getBytes(), SignatureAlgorithm.HS512.getJcaName());
    }

    public String generateToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token. Please check the token format.", ex);
            throw new InvalidTokenException("Invalid JWT token. Please check the token format.");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token. Please login again.", ex);
            throw new ExpiredTokenException("Expired JWT token. Please login again.");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token. Please check the token type.", ex);
            throw new UnsupportedTokenException("Unsupported JWT token. Please check the token type.");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty. Please check the token content.", ex);
            throw new InvalidTokenException("JWT claims string is empty. Please check the token content.");
        } catch (JwtException ex) {
            logger.error("Error parsing token", ex);
            throw new InvalidTokenException("Error parsing token");
        }
    }
}
