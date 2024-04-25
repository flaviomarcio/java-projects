package com.org.commons.security.auth;

//import com.org.core.helper.UrlHelper;
//import com.org.core.utils.Singletons;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.env.Environment;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//@Configuration
//@EnableWebSecurity
public class RequestConfig {
//    private final Environment environment;
//    @Autowired
//    private Singletons singletons;
//
//    public RequestConfig(Environment environment) {
//        if (environment == null)
//            throw new RuntimeException("Invalid environment");
//        this.environment = environment;
//        this.urlConfigure();
//    }
//
//    private void urlConfigure() {
//        var contextPath = environment.getProperty("server.servlet.context-path", "").trim();
//        if (contextPath.equals("/"))
//            contextPath = "";
//        UrlHelper.setContextPath(contextPath);
//    }
//
//
//    @Bean
//    public RequestFilter tokenAuthenticationFilter() {
//        return new RequestFilter(environment, singletons);
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
//        http.csrf().disable();
//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests().anyRequest().authenticated();
//        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//        http.headers().cacheControl();
//        return http.build();
//    }

}
