package com.org.security.auth;

import com.org.security.util.UrlHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.PostConstruct;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class RequestConfig {
    @Value("${spring.security.debug:false}")
    private boolean securityDebug;

    @Value("${server.servlet.context-path}")
    private String server_servlet_context_path;

    @PostConstruct
    private void postConstruct() {
        UrlHelper.setContextPath(server_servlet_context_path);
    }

    @Bean
    public RequestFilter tokenAuthenticationFilter() {
        return new RequestFilter();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests().anyRequest().authenticated();
        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.headers().cacheControl();
        return http.build();
    }

}
