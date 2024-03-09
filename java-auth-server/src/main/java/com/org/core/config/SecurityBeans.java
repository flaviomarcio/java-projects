package com.org.core.config;

import com.org.core.utils.Singletons;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;

@Configuration
public class SecurityBeans {
    @Autowired
    private Environment environment;

    @Bean
    @Primary
    JwtDecoder jwtAccessTokenDecoder() {
        var securityUtil = Singletons.makeSecurityUtil(environment);
        return securityUtil.makeJwtAccessTokenDecoder();
    }

    @Bean
    @Primary
    JwtEncoder jwtAccessTokenEncoder() {
        var securityUtil = Singletons.makeSecurityUtil(environment);
        return securityUtil.makeJwtAccessTokenEncoder();
    }

}
