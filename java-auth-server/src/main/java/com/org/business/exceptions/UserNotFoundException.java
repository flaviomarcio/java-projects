package com.org.business.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String msg) {
        super(msg);
    }

    public static RuntimeException e(String message) {
        log.error(message);
        return new UserNotFoundException(message);
    }

    public static RuntimeException e(String format, Object... args) {
        return e(String.format(format, args));
    }

    public static RuntimeException e(String format, Class<?> c) {
        return e(format, c.getName());
    }

    public static RuntimeException e(String format, Exception e) {
        return e(format, e.getMessage());
    }

    public static boolean is(Exception e) {
        var __is = (UserNotFoundException) e;
        return __is != null;
    }
}
