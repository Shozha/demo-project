package com.technokratos.agona.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Entity
@Table(name = "user_account")
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UserAccount extends AbstractEntity {

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String hashPassword;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "user_account_roles",
            joinColumns = @JoinColumn(name = "user_account_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_account_id", "role"})
    )
    @Column(name = "role", nullable = false)
    private Set<Role> roles;
}
