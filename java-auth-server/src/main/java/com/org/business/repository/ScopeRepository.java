package com.org.business.repository;

import com.org.business.model.Scope;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ScopeRepository extends JpaRepository<Scope, UUID> {

}
