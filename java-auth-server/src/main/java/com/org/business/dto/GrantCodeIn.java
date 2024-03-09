package com.org.business.dto;

import com.org.business.domain.AuthMode;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class GrantCodeIn {
    private AuthMode authMode;
    private String clientId;
}
