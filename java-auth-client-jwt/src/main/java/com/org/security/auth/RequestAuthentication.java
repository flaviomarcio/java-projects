package com.org.security.auth;

import com.org.security.jpa.SecurityCredentialRepository;
import com.org.security.util.JwtParserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class RequestAuthentication extends AbstractAuthenticationToken {
    private final SecurityCredentialRepository securityCredentialRepository;
    private final Environment environment;
    private RequestToken requestToken;
    public RequestAuthentication(SecurityCredentialRepository securityCredentialRepository, Environment environment, String token) {
        super(null);
        this.securityCredentialRepository = securityCredentialRepository;
        this.environment = environment;
        setAuthenticated(this.parser(token));
    }
    public RequestToken token() {
        return requestToken;
    }
    private boolean parser(String tokenBytes) {

        if (environment == null)
            throw new RuntimeException("No environment ");

        var TOKEN_DEBUG = Boolean.parseBoolean(environment.getProperty("token.debug", "false"));

        Map<String, String> payload;
        if (TOKEN_DEBUG) {
            List<String> args = ContextHolder.getPrincipalNames();
            payload = new HashMap<>();
            for (var arg : args) {
                arg = arg.toLowerCase();
                var envName = String.format("token.principal.%s", arg);
                var argValue = environment.getProperty(envName, "").trim();
                if (argValue.isEmpty()) {
                    log.warn("envName[{}]: not found", envName);
                    return false;
                }
                payload.put(arg, argValue);
            }
            requestToken = RequestToken.builder()
                    .exp(LocalDateTime.now().plusDays(100).toEpochSecond(ZoneOffset.MAX))
                    .iat(LocalDateTime.now().toEpochSecond(ZoneOffset.MAX))
                    .sub(payload.get("sub"))
                    .payload(payload)
                    .build();
        } else {
            requestToken = new JwtParserUtil(tokenBytes).getToken();
        }

        if (requestToken == null || requestToken.isExpired()) {
            log.warn("Invalid token");
            return false;
        }

        var securityCustomer = securityCredentialRepository.findByExternalId(requestToken.getSub()).orElse(null);
        if (securityCustomer == null) {
            log.warn("SecurityCustomer not found");
            return false;
        }

        ContextHolder.setToken(requestToken.getPayload());
        ContextHolder.setPrincipal(securityCustomer);
        this.setAuthenticated(ContextHolder.isValid());
        log.info("User authenticated");
        return this.isAuthenticated();
    }
    @Override
    public Object getCredentials() {
        return ContextHolder.getCredential();
    }
    @Override
    public Object getPrincipal() {
        return ContextHolder.getPrincipal();
    }
}
