package com.org.core.adapters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserToJwtConverter implements Converter<UsernamePasswordAuthenticationToken, Jwt> {

    @Override
    public Jwt convert(UsernamePasswordAuthenticationToken authenticationToken) {
        String username = authenticationToken.getName();
        // Obtém informações adicionais, como papéis/authorities do usuário, do objeto authenticationToken.getAuthorities()
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        // Adicione outras informações adicionais do JWT ao mapa de claims, se necessário

        //return new Jwt(claims);
        return new Jwt(null, null, null, null, null);
    }
}
