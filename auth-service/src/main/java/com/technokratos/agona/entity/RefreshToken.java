package com.technokratos.agona.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Entity
@Table(name = "refresh_token")
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "userAccount")
public class RefreshToken extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_account_id", referencedColumnName = "id", columnDefinition = "uuid")
    private UserAccount userAccount;

    @Column(nullable = false, unique = true)
    private String tokenHash;

    @Column(nullable = false)
    private String fingerprintHash;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private Instant expiryDate;
}
