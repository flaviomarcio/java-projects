package com.app.business.adapters;

import com.app.business.dto.ScheduleItemCheckPointIn;
import com.app.business.service.AgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/agent")
@RequiredArgsConstructor
public class AgentController {
    private final AgentService service;

    @PostMapping("/exec")
    public ResponseEntity<?> exec(UUID id) {
        return service.exec(id).asResultHttp();
    }

    @PostMapping("/exec")
    public ResponseEntity<?> checkPoint(ScheduleItemCheckPointIn in) {
        return service.updateCheckPoint(in).asResultHttp();
    }
}