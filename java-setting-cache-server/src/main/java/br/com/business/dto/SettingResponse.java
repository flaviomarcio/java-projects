package br.com.business.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
public class SettingResponse {
    private UUID id;
    private LocalDateTime dt;
    private UUID md5;
}

