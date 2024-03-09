package com.org.business.repository;

import com.org.business.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByScopeId(UUID scopeId);

    boolean existsByScopeIdAndId(UUID scopeId, UUID id);

    boolean existsByScopeIdAndUsername(UUID scopeId, String username);

    Optional<User> findByScopeIdAndDocument(UUID scopeId, String document);

    Optional<User> findByScopeIdAndEmail(UUID scopeId, String email);

    Optional<User> findByScopeIdAndId(UUID scopeId, UUID id);

    Optional<User> findByScopeIdAndPhoneNumber(UUID scopeId, String phoneNumber);

    Optional<User> findByScopeIdAndUsername(UUID scopeId, String username);
}
