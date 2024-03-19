package com.app.business.adapters;

import com.app.business.service.PerformanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class Controller {
    private final PerformanceService service;

    @GetMapping("/")
    public Object exec(@Valid @RequestBody Object payload) {
        return payload;
    }
}