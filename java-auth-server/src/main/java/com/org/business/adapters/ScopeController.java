package com.org.business.adapters;

import com.org.business.dto.ScopeOut;
import com.org.business.model.Scope;
import com.org.business.service.ScopeService;
import com.org.core.utils.HashUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/scope")
@RequiredArgsConstructor
public class ScopeController {
    private final ScopeService scopeService;

    @PostMapping("/save")
    public ResponseEntity<ScopeOut> save(@RequestBody Scope scope) {
        var out = scopeService.save(scope);
        return (out == null)
                ? ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build()
                : ResponseEntity.ok().body(out);
    }

    @GetMapping("/find")
    public ResponseEntity<ScopeOut> find(String id) {
        return ResponseEntity.ok(ScopeOut.from(scopeService.findById(HashUtil.toUuid(id))));
    }

    @GetMapping("/list")
    public ResponseEntity<List<ScopeOut>> list() {
        return ResponseEntity.ok(scopeService.list());
    }

}
