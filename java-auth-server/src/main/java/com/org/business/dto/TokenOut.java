package com.org.business.dto;


import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenOut {
    private String scope;
    private String accessToken;
    private String refreshToken;
     private String tokenType;
    private Long expiresIn;
}
