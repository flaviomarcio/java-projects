package com.org.business.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum AuthMode {
    UNKNOWN,
    USERID,
    USERNAME,
    DOCUMENT,
    EMAIL,
    PHONE_NUMBER;

    public static AuthMode of(Object value){
        if(value==null)
            return UNKNOWN;
        for(var e:values()){
            if(e.equals(value) || e.name().equalsIgnoreCase(value.toString()))
                return e;
        }
        return null;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static AuthMode get(String value) {
        var e=of(value);
        if(e!=null)
            return e;
        throw fail(value);
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static AuthMode get(int value) {
        var e=of(value);
        if(e!=null)
            return e;
        throw fail(value);
    }

    private static IllegalArgumentException fail(Object value){
        var enums=new StringBuilder();
        for(var e:values())
            enums
                    .append(e.name())
                    .append(" ");
        return new IllegalArgumentException(String.format("Invalid value[%s] for %s, valid parameters: %s ",value, AuthMode.class.getSimpleName(), enums));
    }

}


