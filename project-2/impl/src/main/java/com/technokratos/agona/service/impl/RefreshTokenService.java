package com.technokratos.agona.service.impl;


import com.technokratos.agona.entity.RefreshToken;
import com.technokratos.agona.entity.User;
import com.technokratos.agona.exception.auth.RefreshTokenExpiredException;
import com.technokratos.agona.exception.auth.RefreshTokenNotFoundException;
import com.technokratos.agona.exception.auth.RefreshTokenRevokedException;
import com.technokratos.agona.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${security.jwt.refresh-token-expiration-s:604800}")
    private long refreshTokenExpirationS;

    @Value("${security.jwt.revoked-token-retention-s:86400}")
    private long revokedTokenRetentionS;

    public RefreshToken createRefreshToken(User user) {
        RefreshToken token = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusSeconds(refreshTokenExpirationS))
                .revoked(false)
                .build();

        return refreshTokenRepository.save(token);
    }

    @Transactional(readOnly = true)
    public RefreshToken verifyToken(String tokenValue) {
        RefreshToken token = refreshTokenRepository.findByToken(tokenValue)
                .orElseThrow(RefreshTokenNotFoundException::new);

        if (token.isRevoked()) {
            throw new RefreshTokenRevokedException();
        }

        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new RefreshTokenExpiredException();
        }

        return token;
    }

    public RefreshToken rotateRefreshToken(RefreshToken oldToken) {
        oldToken.setRevoked(true);
        refreshTokenRepository.save(oldToken);

        return createRefreshToken(oldToken.getUser());
    }

    public void revokeToken(String tokenValue) {
        refreshTokenRepository.findByToken(tokenValue).ifPresent(token -> {
            token.setRevoked(true);
            refreshTokenRepository.save(token);
            log.info("Refresh token revoked for user '{}'",
                    token.getUser().getUsername());
        });
    }

    public void revokeAllUserTokens(User user) {
        refreshTokenRepository.revokeAllByUser(user);
        log.info("All refresh tokens revoked for user '{}'", user.getUsername());
    }

    @Transactional
    public int cleanupTokens() {
        Instant now = Instant.now();
        int revokedDeleted = refreshTokenRepository.deleteAllByRevokedIsTrueAndExpiryDateBefore(now.minusSeconds(revokedTokenRetentionS));
        int expiredDeleted = refreshTokenRepository.deleteAllByExpiryDateBefore(now);

        int totalDeleted = revokedDeleted + expiredDeleted;
        log.info("Token cleanup finished. Deleted {} revoked and {} expired tokens.", revokedDeleted, expiredDeleted);

        return totalDeleted;
    }
}
