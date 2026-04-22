package com.technokratos.agona.repository;

import com.technokratos.agona.entity.RefreshToken;
import com.technokratos.agona.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByToken(String token);

    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.revoked = true WHERE rt.user = :user")
    void revokeAllByUser(User user);

    @Modifying
    int deleteAllByExpiryDateBefore(Instant now);

    @Modifying
    @Query("DELETE FROM RefreshToken r WHERE r.revoked = true AND r.expiryDate < :date")
    int deleteAllByRevokedIsTrueAndExpiryDateBefore(@Param("date") Instant date);
}
