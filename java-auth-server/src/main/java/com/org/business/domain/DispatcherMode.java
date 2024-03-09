package com.org.business.domain;

import java.util.HashMap;
import java.util.Map;

public enum DispatcherMode {
    UNKNOWN("UNKNOWN"),
    EMAIL("EMAIL"),
    PHONE_NUMBER("PHONE_NUMBER");
    private static final String CONST_DEFAULT = "PHONE_NUMBER";
    private static final Map<String, DispatcherMode> CONST_ENUM = new HashMap<>();

    static {
        for (var e : DispatcherMode.values())
            CONST_ENUM.put(e.value.toUpperCase(), e);
    }

    private final String value;

    DispatcherMode(String value) {
        this.value = String.valueOf(value).trim().toUpperCase();
    }

    public static DispatcherMode of(String value) {
        return CONST_ENUM.getOrDefault(String.valueOf(value).toUpperCase(), PHONE_NUMBER);
    }

    public String value() {
        return this.value;
    }

    public boolean isValid() {
        return CONST_ENUM.containsKey(this.value.toUpperCase());
    }
}


