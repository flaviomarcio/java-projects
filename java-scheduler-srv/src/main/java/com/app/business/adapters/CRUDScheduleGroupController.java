package com.app.business.adapters;

import com.app.business.config.CRUDConst;
import com.app.business.dto.ScheduleGroupIn;
import com.app.business.service.CRUDScheduleGroupService;
import com.littlecode.parsers.PrimitiveUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/crud/group")
@RequiredArgsConstructor
public class CRUDScheduleGroupController {

    private final CRUDScheduleGroupService service;

    @PostMapping("/")
    public ResponseEntity<?> post(@RequestBody ScheduleGroupIn in) {
        return service.saveIn(in).asResultHttp();
    }

    @PutMapping("/")
    public ResponseEntity<?> put(@RequestBody ScheduleGroupIn in) {
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