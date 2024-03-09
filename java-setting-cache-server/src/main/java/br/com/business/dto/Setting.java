package br.com.business.dto;

import lombok.*;

import java.io.Serializable;
import java.util.HashMap;

@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Setting implements Serializable {
    private String name;
    private Object settings;

    public Object getSettings() {
        return (settings == null)
                ? new HashMap<>()
                : settings;
    }
}
