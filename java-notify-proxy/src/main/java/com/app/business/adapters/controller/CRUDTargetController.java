package com.app.business.adapters.controller;

import com.app.business.domain.CRUDConst;
import com.app.business.dto.NotifyTargetIn;
import com.app.business.service.CRUDTargetService;
import com.littlecode.parsers.PrimitiveUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/crud/target")
@RequiredArgsConstructor
public class CRUDTargetController {

    private final CRUDTargetService service;

    @PostMapping("/")
    public ResponseEntity<?> post(@RequestBody NotifyTargetIn in) {
        return ResponseEntity.ok(service.saveIn(in));
    }

    @PutMapping("/")
    public ResponseEntity<?> put(@RequestBody NotifyTargetIn in) {
        return ResponseEntity.ok(service.saveIn(in));
    }

    @GetMapping()
    public ResponseEntity<?> find(@RequestParam String id) {
        return ResponseEntity.ok(service.findIn(PrimitiveUtil.toUUID(id)));
    }

    @DeleteMapping("/")
    public ResponseEntity<?> disabled(@RequestParam String id) {
        return ResponseEntity.ok(service.disable(id));
    }

    @GetMapping(CRUDConst.PATH_LIST)
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(service.list());
    }

    @GetMapping("/dispatchers")
    public ResponseEntity<?> dispatchers() {
        return ResponseEntity.ok(service.dispatchers());
    }
}