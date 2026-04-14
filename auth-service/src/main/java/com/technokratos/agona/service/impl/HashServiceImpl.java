package com.technokratos.agona.service.impl;

import com.technokratos.agona.service.HashService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class HashServiceImpl implements HashService {

    private final Mac hmac;

    @Override
    public String hmacHash(String data) {
        byte[] hash = hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
    }
}
