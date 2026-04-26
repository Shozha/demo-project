package com.technokratos.agona.service;

import com.technokratos.agona.entity.UserAccount;

public interface JwtService {

    String generateAccessToken(UserAccount user);
}
