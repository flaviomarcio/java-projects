package com.org.business.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserInvalidException extends RuntimeException {
    public UserInvalidException(String msg) {
        super(msg);
    }

    public static RuntimeException e(String format, Object... args) {
        return new UserInvalidException(String.format(format, args));
    }

    public static RuntimeException e(String format, Class<?> c) {
        return e(format, c.getName());
    }

    public static RuntimeException e(String format, Exception e) {
        return e(format, e.getMessage());
    }
}
