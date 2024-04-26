package com.app.business.adapters;

import com.app.business.config.AppConfig;
import com.app.business.dto.EventDTO;
import com.app.business.service.EventWritterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

@RestController()
@RequiredArgsConstructor
public class WebHookController {
    private final EventWritterService eventWritterService;
    private final AppConfig.FluxConfig fluxConfig;

    @GetMapping(value = "/events/read", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<List<EventDTO>> events() {
        return Flux
                .interval(Duration.ofSeconds(fluxConfig.getSecs()))
                .map(sequence -> eventWritterService.read(fluxConfig.getMax()))
                .log();
    }

}
