package com.technokratos.agona.repository;

import com.technokratos.agona.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByTokenHashAndActiveTrue(String tokenHash);

    @Query("SELECT COUNT(rt) FROM RefreshToken rt WHERE rt.userAccount.id = :userId AND rt.active = true")
    long countActiveByUserId(@Param("userId") UUID userId);

    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.active = false WHERE rt.userAccount.id = :userId AND rt.active = true")
    void deactivateAllByUserId(@Param("userId") UUID userId);

    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.active = false WHERE rt.id = :id")
    void deactivateById(@Param("id") UUID id);
}
