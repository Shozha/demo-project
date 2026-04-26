package com.technokratos.agona.service.impl;

import com.technokratos.agona.entity.RefreshToken;
import com.technokratos.agona.entity.UserAccount;
import com.technokratos.agona.exception.token.InvalidRefreshTokenException;
import com.technokratos.agona.exception.token.RefreshTokenNotFoundException;
import com.technokratos.agona.exception.token.TooManyRefreshTokensException;
import com.technokratos.agona.properties.RefreshTokenProperties;
import com.technokratos.agona.repository.RefreshTokenRepository;
import com.technokratos.agona.service.HashService;
import com.technokratos.agona.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenProperties properties;
    private final HashService hashService;
    private final SecureRandom secureRandom;

    @Override
    @Transactional(noRollbackFor = TooManyRefreshTokensException.class)
    public String createRefreshToken(UserAccount user, String fingerprint) {
        long activeCount = refreshTokenRepository.countActiveByUserId(user.getId());

        if (activeCount >= properties.getMaxTokensPerUser()) {
            log.warn("User {} reached max active tokens ({}), deactivating all",
                    user.getId(), properties.getMaxTokensPerUser());
            refreshTokenRepository.deactivateAllByUserId(user.getId());
            throw new TooManyRefreshTokensException(properties.getMaxTokensPerUser());
        }

        String rawToken = generateSecureToken();
        String tokenHash = hashService.hmacHash(rawToken);
        String fingerprintHash = hashService.hmacHash(fingerprint);

        RefreshToken refreshToken = RefreshToken.builder()
                .userAccount(user)
                .tokenHash(tokenHash)
                .fingerprintHash(fingerprintHash)
                .active(true)
                .expiryDate(Instant.now().plusMillis(properties.getExpirationMs()))
                .build();

        refreshTokenRepository.save(refreshToken);
        log.debug("Created refresh token for user {}", user.getId());

        return rawToken;
    }

    @Override
    @Transactional
    public String rotateRefreshToken(String rawOldToken, String fingerprint) {
        RefreshToken oldToken = validateAndGet(rawOldToken, fingerprint);

        refreshTokenRepository.deactivateById(oldToken.getId());
        log.debug("Deactivated old refresh token {}", oldToken.getId());

        return createRefreshToken(oldToken.getUserAccount(), fingerprint);
    }

    @Override
    @Transactional(readOnly = true)
    public RefreshToken validateAndGet(String rawToken, String fingerprint) {
        String tokenHash = hashService.hmacHash(rawToken);

        RefreshToken token = refreshTokenRepository.findByTokenHashAndActiveTrue(tokenHash)
                .orElseThrow(RefreshTokenNotFoundException::new);

        if (token.getExpiryDate().isBefore(Instant.now())) {
            throw new InvalidRefreshTokenException();
        }

        String fingerprintHash = hashService.hmacHash(fingerprint);
        if (!token.getFingerprintHash().equals(fingerprintHash)) {
            log.warn("Fingerprint mismatch for refresh token {}", token.getId());
            throw new InvalidRefreshTokenException();
        }

        return token;
    }

    @Override
    @Transactional
    public void deactivate(String rawToken, String fingerprint) {
        RefreshToken token = validateAndGet(rawToken, fingerprint);
        refreshTokenRepository.deactivateById(token.getId());
        log.debug("Deactivated refresh token {} (logout)", token.getId());
    }

    private String generateSecureToken() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
