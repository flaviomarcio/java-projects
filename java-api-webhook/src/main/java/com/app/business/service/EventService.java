package com.app.business.service;


import com.app.business.dto.NotifyEventIn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class EventService {
    public List<NotifyEventIn> take() {
        return List.of(
                NotifyEventIn.builder().id(UUID.randomUUID()).dt(LocalDateTime.now()).build(),
                NotifyEventIn.builder().id(UUID.randomUUID()).dt(LocalDateTime.now()).build(),
                NotifyEventIn.builder().id(UUID.randomUUID()).dt(LocalDateTime.now()).build()
        );
    }

}
