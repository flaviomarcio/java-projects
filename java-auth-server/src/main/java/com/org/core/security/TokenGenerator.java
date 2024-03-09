package com.org.core.security;

import com.org.business.dto.TokenOut;
import com.org.business.exceptions.UnAuthorizedException;
import com.org.business.model.Scope;
import com.org.business.model.User;
import com.org.core.config.SettingService;
import com.org.core.utils.HashUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class TokenGenerator {
    private static final String TOKEN_TYPE = "rsa";
    private final SettingService settingService;
    private final JwtEncoder accessTokenEncoder;
    private final JwtEncoder refreshTokenEncoder;


    private static Jwt convertAuthenticationToJwt(Authentication authentication) {
        Object credentials = authentication.getCredentials();
        if (credentials instanceof Jwt) {
            return (Jwt) credentials;
        }
        return null;
    }

    private String getIssuer() {
        var name = settingService.getAuth_server_token_application_name();
        return name == null || name.isEmpty()
                ? "service"
                : name;
    }

    private int getExpiresMinutes() {
        return settingService.getAuth_server_token_expires_minutes() <= 0
                ? 5
                : settingService.getAuth_server_token_expires_minutes();
    }

    private int getExpiresDays() {
        return settingService.getAuth_server_token_expires_days() <= 0
                ? 7
                : settingService.getAuth_server_token_expires_days();
    }

    private JwtClaimsSet createClaims(Instant issuedAt, Instant expiresAt, Authentication authentication) {
        User user = User.cast(authentication.getPrincipal());
        Instant now = Instant.now();
        return JwtClaimsSet.builder()
                .issuer(getIssuer())
                .issuedAt(now)
                .expiresAt(now.plus(getExpiresMinutes(), ChronoUnit.MINUTES))
                .subject(user.getId().toString())
                .build();
    }

    private String createAccessToken(Instant issuedAt, Instant expiresAt, Authentication authentication) {
        JwtClaimsSet claimsSet = createClaims(issuedAt, expiresAt, authentication);
        return accessTokenEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }

    private String createRefreshToken(Instant issuedAt, Instant expiresAt, Authentication authentication) {
        JwtClaimsSet claimsSet = createClaims(issuedAt, expiresAt, authentication);
        return refreshTokenEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }

    public TokenOut createToken(Scope scope, Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null)
            throw UnAuthorizedException.e("Invalid principal");
        User user = User.cast(authentication.getPrincipal());
        if (user == null) {
            throw UnAuthorizedException.e("principal {0} is not of User type", authentication.getPrincipal().getClass());
        }
        var issuedAt = Instant.now();
        var expiresAt = issuedAt.plus(getExpiresDays(), ChronoUnit.DAYS);

        var accessToken = createAccessToken(issuedAt, expiresAt, authentication);
        var refreshToken = createRefreshToken(issuedAt, expiresAt, authentication);

        return TokenOut
                .builder()
                .tokenType(TOKEN_TYPE)
                .scope(scope.getName())
                .expiresIn(expiresAt.toEpochMilli())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
