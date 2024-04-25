package com.org.commons.security;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.org.commons.adapters.JwtToUserConverter;
import com.org.commons.config.SettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class SecurityUtil {
    private final KeyUtils keyUtils;
    private final JwtToUserConverter jwtToUserConverter = new JwtToUserConverter();

    public TokenGenerator makeTokenGenerator(SettingService settingService) {
        return new TokenGenerator(settingService, makeJwtAccessTokenEncoder(), makeJwtRefreshTokenEncoder());
    }

    public JwtDecoder makeJwtAccessTokenDecoder() {
        return NimbusJwtDecoder.withPublicKey(keyUtils.getAccessTokenPublicKey()).build();
    }

    public JwtEncoder makeJwtAccessTokenEncoder() {
        JWK jwk = new RSAKey
                .Builder(keyUtils.getAccessTokenPublicKey())
                .privateKey(keyUtils.getAccessTokenPrivateKey())
                .build();
        return new NimbusJwtEncoder(new ImmutableJWKSet<>(new JWKSet(jwk)));
    }

    public JwtDecoder makeJwtRefreshTokenDecoder() {
        return NimbusJwtDecoder.withPublicKey(keyUtils.getRefreshTokenPublicKey()).build();
    }

    public JwtEncoder makeJwtRefreshTokenEncoder() {
        JWK jwk = new RSAKey
                .Builder(keyUtils.getRefreshTokenPublicKey())
                .privateKey(keyUtils.getRefreshTokenPrivateKey())
                .build();
        return new NimbusJwtEncoder(new ImmutableJWKSet<>(new JWKSet(jwk)));
    }

    public JwtAuthenticationProvider makeJwtRefreshTokenAuthProvider() {
        JwtAuthenticationProvider provider = new JwtAuthenticationProvider(makeJwtRefreshTokenDecoder());
        provider.setJwtAuthenticationConverter(jwtToUserConverter);
        return provider;
    }

//    public DaoAuthenticationProvider makeDaoAuthenticationProvider(UserDetailsManager userDetailsManager) {
//        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
//        daoAuthenticationProvider.setPasswordEncoder(this.makePasswordEncoder());
//        daoAuthenticationProvider.setUserDetailsService(userDetailsManager);
//        return daoAuthenticationProvider;
//    }

//    public String stringEncode(String value){
//        return this.makePasswordEncoder().encode(value);
//    }

}
