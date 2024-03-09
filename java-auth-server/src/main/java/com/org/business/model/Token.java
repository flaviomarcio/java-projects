package com.org.business.model;


import com.org.business.dto.TokenOut;
import com.org.core.utils.HashUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Entity
@Table(name = "token")
@Builder
@Getter
@Setter
@AllArgsConstructor
public class Token {
    @Id
    @Column(nullable = false)
    private UUID id;
    @Column(nullable = false)
    private LocalDateTime dt;
    @Column(nullable = false)
    private UUID scopeId;
    @Column(nullable = false)
    private UUID userId;
    //@Column(nullable = false)
    private UUID grantCodeId;
    @Column(nullable = false)
    private String tokenType;
    @Column(nullable = false, length = 1000)
    private String accessToken;
    private UUID accessTokenMd5;
    @Column(nullable = false, length = 1000)
    private String refreshToken;
    private UUID refreshTokenMd5;
    @Column(nullable = false)
    private Long expiresIn;
    @Column(nullable = false)
    private boolean enabled;

    public Token() {

    }

    public static Token from(GrantCode grantCode, TokenOut tokenOut) {
        return Token.builder()
                .id(UUID.randomUUID())
                .dt(LocalDateTime.now())
                .scopeId(grantCode.getScopeId())
                .userId(grantCode.getUserId())
                .grantCodeId(grantCode.getId())
                .enabled(true)

                .expiresIn(tokenOut.getExpiresIn())
                .tokenType(tokenOut.getTokenType())
                .accessToken(tokenOut.getAccessToken())
                .accessTokenMd5(HashUtil.toMd5Uuid(tokenOut.getAccessToken()))
                .refreshToken(tokenOut.getRefreshToken())
                .refreshTokenMd5(HashUtil.toMd5Uuid(tokenOut.getRefreshToken()))
                .build();
    }

    public static Token from(User user, TokenOut tokenOut) {
        return Token.builder()
                .id(UUID.randomUUID())
                .dt(LocalDateTime.now())
                .scopeId(user.getScopeId())
                .userId(user.getId())
                .grantCodeId(null)
                .enabled(true)

                .expiresIn(tokenOut.getExpiresIn())
                .tokenType(tokenOut.getTokenType())
                .accessToken(tokenOut.getAccessToken())
                .accessTokenMd5(HashUtil.toMd5Uuid(tokenOut.getAccessToken()))
                .refreshToken(tokenOut.getRefreshToken())
                .refreshTokenMd5(HashUtil.toMd5Uuid(tokenOut.getRefreshToken()))

                .build();
    }

    public boolean isExpired() {
        var tokenDt = LocalDateTime.ofEpochSecond(this.expiresIn, 0, ZoneOffset.MAX);
        return tokenDt.isBefore(LocalDateTime.now());
    }

}
