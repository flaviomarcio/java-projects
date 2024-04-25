package com.app.business.adapters;

import com.app.business.service.IngesterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class Controller {
    private final IngesterService service;

    @GetMapping("/")
    public ResponseEntity<?> exec(@Valid @RequestBody Object payload) {
        return service.proxy(payload).asResultHttp();
    }
}