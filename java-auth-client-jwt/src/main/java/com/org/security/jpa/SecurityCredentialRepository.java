package com.org.security.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SecurityCredentialRepository extends JpaRepository<SecurityCredential, UUID> {
    Optional<Object> findByExternalId(String externalId);
}
