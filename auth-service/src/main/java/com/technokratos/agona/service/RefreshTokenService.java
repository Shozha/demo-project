package com.technokratos.agona.service;

import com.technokratos.agona.entity.RefreshToken;
import com.technokratos.agona.entity.UserAccount;

public interface RefreshTokenService {

    /**
     * Создаёт новый refresh-токен для пользователя и привязывает к fingerprint.
     *
     * @return сырой (не хэшированный) refresh-токен для передачи клиенту
     */
    String createRefreshToken(UserAccount user, String fingerprint);

    /**
     * Ротация: находит старый токен по сырому значению, проверяет fingerprint,
     * деактивирует старый и создаёт новый.
     *
     * @return сырой новый refresh-токен
     */
    String rotateRefreshToken(String rawOldToken, String fingerprint);

    /**
     * Находит активный refresh-токен по сырому значению и проверяет fingerprint.
     */
    RefreshToken validateAndGet(String rawToken, String fingerprint);

    /**
     * Деактивирует конкретный refresh-токен (logout).
     */
    void deactivate(String rawToken, String fingerprint);
}
