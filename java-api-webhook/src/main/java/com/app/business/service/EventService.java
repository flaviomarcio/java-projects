package com.app.business.service;


import com.app.business.dto.NotifyEventIn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class EventService {
    public List<NotifyEventIn> take() {
        return new ArrayList<>();
    }

}
