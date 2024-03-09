package com.org.security.jpa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@Table(schema = "auth", name = "credential")
@AllArgsConstructor
public class SecurityCredential {
    @Id
    private UUID id;
    private boolean enabled;
    private UUID customerId;
    private String document;
    private String externalId;

    public SecurityCredential() {
    }
}
