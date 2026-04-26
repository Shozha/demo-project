package com.technokratos.agona.service.impl;

import com.technokratos.agona.entity.RefreshToken;
import com.technokratos.agona.entity.Role;
import com.technokratos.agona.entity.UserAccount;
import com.technokratos.agona.exception.authentication.InvalidCredentialsException;
import com.technokratos.agona.exception.authentication.UserAccountNotFoundException;
import com.technokratos.agona.exception.registration.EmailAlreadyExistException;
import com.technokratos.agona.exception.registration.NicknameAlreadyExistException;
import com.technokratos.agona.mapper.UserAccountMapper;
import com.technokratos.agona.repository.UserAccountRepository;
import com.technokratos.agona.service.AuthenticationService;
import com.technokratos.agona.service.JwtService;
import com.technokratos.agona.service.RefreshTokenService;
import com.technokratos.agona.dto.request.RefreshTokenRequest;
import com.technokratos.agona.dto.request.SignInRequest;
import com.technokratos.agona.dto.request.SignUpRequest;
import com.technokratos.agona.dto.response.TokenPair;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserAccountRepository userAccountRepository;
    private final UserAccountMapper userAccountMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Override
    @Transactional
    public TokenPair signUp(SignUpRequest request) {
        if (userAccountRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistException(request.email());
        }
        if (userAccountRepository.existsByNickname(request.nickname())) {
            throw new NicknameAlreadyExistException(request.nickname());
        }

        UserAccount user = userAccountMapper.toEntity(request);
        user.setHashPassword(passwordEncoder.encode(request.password()));
        user.setRoles(Set.of(Role.ROLE_STUDENT));

        user = userAccountRepository.save(user);
        log.info("User registered: {} ({})", user.getNickname(), user.getEmail());

        return generateTokenPair(user, request.fingerprint());
    }

    @Override
    @Transactional(readOnly = true)
    public TokenPair signIn(SignInRequest request) {
        UserAccount user = userAccountRepository.findByEmail(request.email())
                .orElseThrow(() -> new UserAccountNotFoundException("email", request.email()));

        if (!passwordEncoder.matches(request.password(), user.getHashPassword())) {
            throw new InvalidCredentialsException();
        }

        log.info("User signed in: {} ({})", user.getNickname(), user.getEmail());
        return generateTokenPair(user, request.fingerprint());
    }

    @Override
    @Transactional
    public TokenPair refreshToken(RefreshTokenRequest request, String rawRefreshToken) {
        RefreshToken oldToken = refreshTokenService.validateAndGet(rawRefreshToken, request.fingerprint());
        UserAccount user = oldToken.getUserAccount();

        String newRawRefresh = refreshTokenService.rotateRefreshToken(rawRefreshToken, request.fingerprint());
        String accessToken = jwtService.generateAccessToken(user);

        log.debug("Token rotated for user {}", user.getId());
        return new TokenPair(accessToken, newRawRefresh);
    }

    @Override
    @Transactional
    public void logout(RefreshTokenRequest request, String rawRefreshToken) {
        refreshTokenService.deactivate(rawRefreshToken, request.fingerprint());
        log.info("User logged out (token deactivated)");
    }

    private TokenPair generateTokenPair(UserAccount user, String fingerprint) {
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = refreshTokenService.createRefreshToken(user, fingerprint);
        return new TokenPair(accessToken, refreshToken);
    }
}
