package com.org.business.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
public class GrantCodeOut {
    private UUID code;
    private String redirect_uri;
}
