package com.technokratos.agona.security.provider;

import com.technokratos.agona.security.userdetails.UserDetailsImpl;
import io.jsonwebtoken.Claims;

import java.util.List;
import java.util.UUID;

public interface JwtAccessTokenProvider {

    String generateAccessToken(UserDetailsImpl userDetails);

    boolean validateAccessToken(String token);

    Claims parseAccessToken(String token);

    String getUsernameFromToken(String token);

    List<String> getRolesFromToken(String token);

    UUID getUserIdFromToken(String token);

}
