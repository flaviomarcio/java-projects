package com.app.business.adapters;

import com.app.business.model.ScheduleItem;
import com.app.business.service.CRUDScheduleItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/config")
@RequiredArgsConstructor
public class ConfigController {
    private final CRUDScheduleItemService service;

    @PostMapping("/save")
    public ResponseEntity<?> post(@Valid @RequestBody ScheduleItem scheduleItem) {
        return service.save(scheduleItem).asResultHttp();
    }

    @PutMapping("/save")
    public ResponseEntity<?> put(@Valid @RequestBody ScheduleItem scheduleItem) {
        return service.save(scheduleItem).asResultHttp();
    }

    @GetMapping("/list")
    public ResponseEntity<?> list() {
        return service.list().asResultHttp();
    }
}