package com.org.business.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthGenericException extends RuntimeException {
    public AuthGenericException(String msg) {
        super(msg);
        log.error(msg);
    }

    public static RuntimeException e(String format, Object... args) {
        return new AuthGenericException(String.format(format, args));
    }

    public static RuntimeException e(String format, Class<?> c) {
        return e(format, c.getName());
    }

    public static RuntimeException e(String format, Exception e) {
        return e(format, e.getMessage());
    }
}
