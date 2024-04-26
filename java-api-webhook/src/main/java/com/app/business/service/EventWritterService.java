package com.app.business.service;


import com.app.business.dto.EventDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class EventWritterService {
    public List<EventDTO> read(int fluxCount) {
        List<EventDTO> __return = new ArrayList<>();
        for (int i = 1; i < fluxCount; i++) {
            __return.add(EventDTO.builder().id(UUID.randomUUID()).dt(LocalDateTime.now()).build());
        }
        return __return;
    }

}
