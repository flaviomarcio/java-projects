package com.app.business.adapters;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/exec")
@RequiredArgsConstructor
public class Controller {

    @GetMapping("/")
    public Object get() {
        return Map.of("id", UUID.randomUUID());
    }

    @DeleteMapping("/")
    public Object delete() {
        return Map.of("id", UUID.randomUUID());
    }

    @PostMapping("/")
    public Object post(@RequestBody(required = false) String payload) {
        return Map.of("size", payload == null ? 0 : payload.length());
    }

    @PutMapping("/")
    public Object put(@RequestBody(required = false) String payload) {
        return Map.of("size", payload == null ? 0 : payload.length());
    }


}