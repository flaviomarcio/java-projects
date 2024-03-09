package com.org.controller;

import com.org.security.auth.ContextHolder;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "JWT-Test")
public class Controller {

    @GetMapping("/check")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Object check() {
        return ContextHolder.getPrincipal();
    }
}
