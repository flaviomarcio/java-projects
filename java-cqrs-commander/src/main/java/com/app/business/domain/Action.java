package com.app.business.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
public enum Action {
    SELECT(0),
    INSERT(1),
    UPDATE(2),
    UPSERT(3),
    DELETE(4);
    @JsonValue
    private final int value;

    Action(int value) {
        this.value = value;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static Action get(int value) {
        return Arrays.stream(values()).filter(t -> Objects.equals(t.getValue(), value)).findFirst().orElse(null);
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static Action get(String value) {
        if (value == null)
            return null;
        return Arrays.stream(values()).filter(t -> t.name().equalsIgnoreCase(value)).findFirst().orElse(null);
    }
}
