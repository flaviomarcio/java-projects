package com.app.business.adapters;

import com.app.business.dto.Command;
import com.app.business.service.IngesterService;
import com.littlecode.parsers.PrimitiveUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/commander")
@RequiredArgsConstructor
public class Controller {

    private final IngesterService ingesterService;

    @GetMapping()
    public ResponseEntity<?> get(@RequestParam String id) {
        return ingesterService.select(PrimitiveUtil.toUUID(id)).asResultHttp();
    }

    @PostMapping("/")
    public ResponseEntity<?> post(@RequestBody Command in) {
        return ingesterService.insert(in).asResultHttp();
    }

    @PutMapping("/")
    public ResponseEntity<?> put(@RequestBody Command in) {
        return ingesterService.update(in).asResultHttp();
    }

    @DeleteMapping("/")
    public ResponseEntity<?> delete(@RequestBody Command in) {
        return ingesterService.delete(in).asResultHttp();
    }
}