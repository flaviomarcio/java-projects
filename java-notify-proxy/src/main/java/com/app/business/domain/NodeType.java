package com.app.business.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum NodeType {
    OnetoAll("onetoall"), MASTER("master"), NODE("node");
    @JsonValue
    private final String value;

    NodeType(String value) {
        this.value = value;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static NodeType get(String id) {
        return Arrays.stream(values()).filter(t -> t.getValue().toString().equalsIgnoreCase(id)).findFirst().orElse(null);
    }
}
