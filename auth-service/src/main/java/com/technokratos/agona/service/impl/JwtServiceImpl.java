package com.technokratos.agona.service.impl;

import com.technokratos.agona.entity.UserAccount;
import com.technokratos.agona.properties.JwtProperties;
import com.technokratos.agona.service.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Service
public class JwtServiceImpl implements JwtService {

    private final SecretKey signingKey;
    private final long accessTokenExpirationMs;

    public JwtServiceImpl(JwtProperties jwtProperties) {
        this.signingKey = Keys.hmacShaKeyFor(
                jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8)
        );
        this.accessTokenExpirationMs = jwtProperties.getAccessTokenExpirationMs();
    }

    @Override
    public String generateAccessToken(UserAccount user) {
        Instant now = Instant.now();
        Instant expiry = now.plusMillis(accessTokenExpirationMs);

        String roles = user.getRoles().stream()
                .map(Enum::name)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("username", user.getNickname())
                .claim("roles", roles)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(signingKey)
                .compact();
    }
}
