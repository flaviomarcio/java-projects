package com.app.business.adapters.consumer;

import com.app.business.dto.NotifyEventIn;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
public class WebHookController {
    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<NotifyEventIn> events() {
        return Flux
                .interval(Duration.ofSeconds(1))
                .map(sequence -> NotifyEventIn.builder().build())
                .log();
    }

}
