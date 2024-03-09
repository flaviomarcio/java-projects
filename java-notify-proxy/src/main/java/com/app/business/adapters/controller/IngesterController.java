package com.app.business.adapters.controller;

import com.app.business.dto.NotifyEventIn;
import com.app.business.service.EventIngesterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
public class IngesterController {

    private final EventIngesterService service;

    @PostMapping("/insert")
    public ResponseEntity<?> insert(@RequestBody NotifyEventIn eventIn) {
        return service.register(eventIn).asResultHttp();
    }

}