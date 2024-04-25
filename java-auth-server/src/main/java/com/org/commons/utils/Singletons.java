package com.org.commons.utils;

import com.org.business.model.Scope;
import com.org.business.service.*;
import com.org.commons.config.SettingService;
import com.org.commons.security.KeyUtils;
import com.org.commons.security.SecurityUtil;
import com.org.commons.security.TokenGenerator;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Getter
@Service
@Configuration
public class Singletons {
    private final Environment environment;
    private final RepositoryService repositoryService;
    private SettingService settingService;
    private ScopeService scopeService;
    private UserService userService;
    private KeyUtils keyUtils;
    private SecurityUtil securityUtil;
    private TokenGenerator tokenGenerator;
    private JwtAuthenticationProvider jwtRefreshTokenAuthProvider;
    private AuthenticationService authenticationService;
    private DispatcherService dispatcherService;


    public Singletons(Environment environment, RepositoryService repositoryService) {
        this.environment = environment;
        this.repositoryService = repositoryService;
        this.init();
    }

    public static Singletons make(Environment environment, RepositoryService repositoryService) {
        return new Singletons(environment, repositoryService).init();
    }

    public static SecurityUtil makeSecurityUtil(Environment environment) {
        var settingService = new SettingService(environment);
        settingService.init();
        return new SecurityUtil(new KeyUtils(settingService));
    }

    public Singletons init() {
        settingService = new SettingService(environment);
        settingService.init();
        keyUtils = new KeyUtils(settingService);
        securityUtil = new SecurityUtil(keyUtils);
        tokenGenerator = securityUtil.makeTokenGenerator(settingService);
        jwtRefreshTokenAuthProvider = securityUtil.makeJwtRefreshTokenAuthProvider();
        authenticationService = new AuthenticationService(this);
        scopeService = new ScopeService(this);
        userService = new UserService(this);
        dispatcherService = new DispatcherService(settingService);
        return this;
    }

    public UUID getScopeSystem() {
        return settingService == null
                ? null
                : settingService.getScopeId();
    }

    public Scope getScope() {
        return scopeService.getScopeSystem();
    }
}
