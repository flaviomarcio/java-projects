package com.org.commons.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@Slf4j
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//@Configuration
public class WebConfig {
//    @Autowired
//    private Environment environment;
//
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        var contextPath = environment.getProperty("server.servlet.context-path", "");
//        UrlHelper.setContextPath(contextPath);
//        http
//                .cors().disable()
//                .csrf().disable()
//                .formLogin().disable()
//                .httpBasic().disable()
//                .authorizeRequests()
//                .antMatchers(UrlHelper.getTrustedUrl()).permitAll();
//        return http.build();
//    }

}
