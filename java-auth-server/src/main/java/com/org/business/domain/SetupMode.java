package com.org.business.domain;

import java.util.HashMap;
import java.util.Map;

public enum SetupMode {
    AUTO("AUTO"),
    DISABLED("DISABLED");

    private static final String CONST_DEFAULT = "DISABLED";
    private static final Map<String, SetupMode> CONST_ENUM = new HashMap<>();

    static {
        for (var e : SetupMode.values())
            CONST_ENUM.put(e.value.toUpperCase(), e);
    }

    private final String value;

    SetupMode(String value) {
        this.value = String.valueOf(value).trim().toUpperCase();
    }

    public static SetupMode of(String value) {
        return CONST_ENUM.getOrDefault(String.valueOf(value).toUpperCase(), DISABLED);
    }

    public boolean isValid() {
        return CONST_ENUM.containsKey(this.value.toUpperCase());
    }

    public String value() {
        return this.value;
    }
}


