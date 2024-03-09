package com.org.business.repository;

import com.org.business.model.GrantCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GrantCodeRepository extends JpaRepository<GrantCode, UUID> {

}
