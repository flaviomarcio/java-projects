package com.org.business.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UnAuthorizedException extends RuntimeException {
    public UnAuthorizedException(String msg) {
        super(msg);
        log.error(msg);
    }

    public static RuntimeException e(String msg) {
        return new UnAuthorizedException(msg);
    }

    public static RuntimeException e(String format, Object... args) {
        return new UnAuthorizedException(String.format(format, args));
    }

    public static RuntimeException e(String format, Class<?> c) {
        return e(format, c.getName());
    }

    public static RuntimeException e(String format, Exception e) {
        return e(format, e.getMessage());
    }

    public static boolean is(Exception e) {
        var __is = (UnAuthorizedException) e;
        return __is != null;
    }
}
