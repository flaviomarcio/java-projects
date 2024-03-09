package com.org.business.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InvalidInformationException extends RuntimeException {
    public InvalidInformationException(String msg) {
        super(msg);
        log.error(msg);
    }

    public static RuntimeException e(String format, Object... args) {
        return new InvalidInformationException(String.format(format, args));
    }

    public static RuntimeException e(String format, Class<?> c) {
        return e(format, c.getName());
    }

    public static RuntimeException e(String format, Exception e) {
        return e(format, e.getMessage());
    }

    public static boolean is(Exception e) {
        var __is = (InvalidInformationException) e;
        return __is != null;
    }
}
