package com.org.security.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.org.security.auth.RequestToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class JwtParserUtil implements Serializable {
    public static final String BEARER = "Bearer ";
    private final String tokenData;
    public JwtParserUtil(String tokenData) {
        super();
        this.tokenData = tokenData;
    }
    public static String getBearer() {
        return BEARER;
    }
    public RequestToken getToken() {
        DecodedJWT decodedJWT = decodeToken(tokenData);
        JsonObject payloadAsJson = decodeTokenPayloadToJsonObject(decodedJWT);
        Map<String, String> payload = new HashMap<>();
        payloadAsJson.asMap().forEach((key, value) -> {
            payload.put(key, value.getAsString());
        });
        return RequestToken.builder()
                .iss(payloadAsJson.get("iss").getAsString())
                .sub(payloadAsJson.get("sub").getAsString())
                .exp(payloadAsJson.get("exp").getAsLong())
                .iat(payloadAsJson.get("iat").getAsLong())
                .token(tokenData)
                .payload(payload)
                .build();
    }
    public String getTokenData() {
        return tokenData;
    }
    public Collection<? extends GrantedAuthority> getAuthorities() {
        DecodedJWT decodedJWT = decodeToken(tokenData);
        JsonObject payloadAsJson = decodeTokenPayloadToJsonObject(decodedJWT);

        return StreamSupport
                .stream(payloadAsJson.getAsJsonObject("realm_access").getAsJsonArray("roles").spliterator(), false)
                .map(JsonElement::getAsString).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
    private DecodedJWT decodeToken(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new JWTInvalidException("Token has not been provided");
        }
        return JWT.decode(value);
    }
    private JsonObject decodeTokenPayloadToJsonObject(DecodedJWT decodedJWT) {
        try {
            String payloadAsString = decodedJWT.getPayload();
            return new Gson().fromJson(new String(Base64.getDecoder().decode(payloadAsString), StandardCharsets.UTF_8),
                    JsonObject.class);
        } catch (RuntimeException exception) {
            throw new JWTInvalidException("Invalid JWT or JSON format of each of the jwt parts", exception);
        }
    }
    public static class JWTInvalidException extends RuntimeException {
        public JWTInvalidException(String msg, Throwable t) {
            super(msg, t);
        }

        public JWTInvalidException(String msg) {
            super(msg);
        }
    }
}