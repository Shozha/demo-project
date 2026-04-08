package com.technokratos.agona.security.provider;

import com.technokratos.agona.security.userdetails.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class JwtAccessTokenProviderImpl implements JwtAccessTokenProvider {

    @Value("${security.jwt.secret}")
    private String jwtSecret;

    @Getter
    @Value("${security.jwt.access-token-expiration-ms:900000}")
    private long accessTokenExpirationMs;

    private Key signingKey;

    private static final String ROLES_CLAIM = "roles";
    private static final String USER_ID_CLAIM = "userId";

    @PostConstruct
    public void init() {
        this.signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    @Override
    public String generateAccessToken(UserDetailsImpl userDetails) {
        Instant now = Instant.now();
        Instant expiry = now.plusMillis(accessTokenExpirationMs);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim(ROLES_CLAIM, roles)
                .claim(USER_ID_CLAIM, userDetails.getId().toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiry))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public boolean validateAccessToken(String token) {
        try {
            parseAccessToken(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.debug("JWT Access Token expired: {}", e.getMessage());
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Invalid JWT Access Token: {}", e.getMessage());
        }
        return false;
    }

    @Override
    public Claims parseAccessToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public String getUsernameFromToken(String token) {
        return parseAccessToken(token).getSubject();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> getRolesFromToken(String token) {
        return (List<String>) parseAccessToken(token).get(ROLES_CLAIM);
    }

    @Override
    public UUID getUserIdFromToken(String token) {
        String userId = (String) parseAccessToken(token).get(USER_ID_CLAIM);
        return UUID.fromString(userId);
    }
}
