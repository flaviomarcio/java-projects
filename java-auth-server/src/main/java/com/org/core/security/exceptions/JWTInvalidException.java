package com.org.core.security.exceptions;

import com.org.business.exceptions.InvalidInformationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JWTInvalidException extends RuntimeException {
    public JWTInvalidException(String msg) {
        super(msg);
    }

    public static RuntimeException e(String message) {
        log.info(message);
        return new InvalidInformationException(message);
    }

    public static RuntimeException e(String format, Object... args) {
        var msg = String.format(format, args);
        log.info(msg);
        return e(msg);
    }

    public static RuntimeException e(String format, Class<?> c) {
        return e(format, c.getName());
    }

    public static RuntimeException e(String format, Exception e) {
        try {
            return e(format, e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
