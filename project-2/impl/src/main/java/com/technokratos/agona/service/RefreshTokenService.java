package com.technokratos.agona.service;

import com.technokratos.agona.entity.RefreshToken;
import com.technokratos.agona.entity.User;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(User user);

    RefreshToken verifyToken(String tokenValue);

    RefreshToken rotateRefreshToken(RefreshToken oldToken);

    void revokeToken(String tokenValue);

    void revokeAllUserTokens(User user);

    int cleanupTokens();

}
