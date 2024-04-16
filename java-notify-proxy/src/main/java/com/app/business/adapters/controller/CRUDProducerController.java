package com.app.business.adapters.controller;

import com.app.business.domain.CRUDConst;
import com.app.business.dto.NotifyProducerIn;
import com.app.business.service.CRUDProducerService;
import com.littlecode.parsers.PrimitiveUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/crud/producer")
@RequiredArgsConstructor
public class CRUDProducerController {

    private final CRUDProducerService service;

    @PostMapping("/")
    public ResponseEntity<?> post(@RequestBody NotifyProducerIn in) {
        return service.saveIn(in).asResultHttp();
    }

    @PutMapping("/")
    public ResponseEntity<?> put(@RequestBody NotifyProducerIn in) {
        return service.saveIn(in).asResultHttp();
    }

    @GetMapping()
    public ResponseEntity<?> find(@RequestParam String id) {
        return service.findIn(PrimitiveUtil.toUUID(id)).asResultHttp();
    }

    @DeleteMapping("/")
    public ResponseEntity<?> disabled(@RequestParam String id) {
        return service.disable(id).asResultHttp();
    }

    @GetMapping(CRUDConst.PATH_LIST)
    public ResponseEntity<?> list() {
        return service.list().asResultPagination();
    }
}