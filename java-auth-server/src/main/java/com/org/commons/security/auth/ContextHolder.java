package com.org.commons.security.auth;

import com.littlecode.parsers.HashUtil;
import com.org.business.exceptions.UserInvalidException;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ContextHolder {

    private static final String CONST_PRINCIPAL_FORMAT = "principal.%s";
    private static final String CONST_SCOPE_ID = "scopeId";
    private static final String CONST_USER_ID = "userId";
    private static final String CONST_USER_NAME = "userName";
    private static final ThreadLocal<Map<String, String>> requestContext = new ThreadLocal<>();

    private ContextHolder() {
        super();
    }

    private static synchronized Map<String, String> getContext() {
        Map<String, String> ctx = requestContext.get();

        if (ctx == null) {
            ctx = new HashMap<>();
            requestContext.set(ctx);
        }

        return ctx;
    }

    public static synchronized UUID getScopeId() {
        return HashUtil.toUuid(getContext().get(String.format(CONST_PRINCIPAL_FORMAT, CONST_SCOPE_ID)));
    }

    public static synchronized UUID getUserId() {
        return HashUtil.toUuid(getContext().get(String.format(CONST_PRINCIPAL_FORMAT, CONST_USER_ID)));
    }

    public static synchronized void setUserId(UUID userId) {
        if (userId == null)
            throw UserInvalidException.e("Invalid userId");
        getContext().put(String.format(CONST_PRINCIPAL_FORMAT, CONST_USER_ID), userId.toString());
    }

    public static synchronized String getUserName() {
        return getContext().get(String.format(CONST_PRINCIPAL_FORMAT, CONST_USER_NAME));
    }

    public static synchronized Map<String, String> getPrincipal() {
        Map<String, String> __return = new HashMap<>();
        var context = getContext();
        String principalPrefix = String.format(CONST_PRINCIPAL_FORMAT, "");
        context.forEach((key, value) -> {
            if (key.startsWith(principalPrefix))
                __return.put(key.replace(principalPrefix, ""), value);
        });
        return __return;
    }

    public static synchronized void setPrincipal(final Object o) {
        if (o == null)
            return;
        var context = getContext();
        Map<String, String> fieldValues = new HashMap<>();
        Field[] fieldList = o.getClass().getDeclaredFields();
        for (Field field : fieldList) {
            field.setAccessible(true);
            String fieldName = field.getName();
            String fieldValue;
            try {
                Object objectValue = field.get(o);
                fieldValue = (objectValue != null)
                        ? objectValue.toString()
                        : null;
            } catch (IllegalAccessException e) {
                fieldValue = null;
            }
            fieldValue = (fieldValue == null) ? "" : fieldValue;
            fieldValues.put(String.format(CONST_PRINCIPAL_FORMAT, fieldName), fieldValue);
        }
        context.putAll(fieldValues);
    }


    public static synchronized Map<String, String> getCredential() {
        return new HashMap<>();
    }

    public static synchronized boolean isValid() {
        return (getScopeId() != null && getUserId() != null);
    }

    public static synchronized void setToken(final Map<String, String> token) {
        var context = getContext();
        context.putAll(token);
    }

}
