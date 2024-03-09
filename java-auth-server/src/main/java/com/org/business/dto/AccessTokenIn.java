package com.org.business.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
public class AccessTokenIn {
    private String grantType;
    private UUID code;
}
