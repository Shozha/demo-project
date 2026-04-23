package com.technokratos.agona.repository;

import com.technokratos.agona.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    @Query("select u from User u join fetch u.roles where u.username = :username")
    Optional<User> findByUsername(String username);

    @Query("select u from User u join fetch u.roles")
    List<User> findAllWithRoles();

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

}
