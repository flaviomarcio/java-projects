package com.app.business.adapters;

import com.app.business.dto.NotifyEventIn;
import com.app.business.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class Controller {
    private final EventService eventService;

    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<List<NotifyEventIn>> events() {
        return Flux
                .interval(Duration.ofSeconds(1))
                .map(sequence -> eventService.take())
                .log();
    }

}
