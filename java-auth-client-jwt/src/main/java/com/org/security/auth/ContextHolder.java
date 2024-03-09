package com.org.security.auth;

import org.springframework.security.core.GrantedAuthority;

import java.lang.reflect.Field;
import java.util.*;

public class ContextHolder {

    private static final String CONST_PRINCIPAL_FORMAT = "principal.%s";
    private static final String CONST_SUB = "sub";
    private static final String CONST_DOCUMENT = "document";
    private static final String CONST_CUSTOMER_ID = "customerId";
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

    public static List<String> getPrincipalNames() {
        return List.of(CONST_SUB);
    }


    public static synchronized Map<String, String> getValues() {
        return getContext();
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

        public static synchronized Collection<? extends GrantedAuthority> getAuthorities(){
        return new HashSet<>();
    }
    public static synchronized void clearContext() {
        requestContext.remove();
    }

    public static synchronized boolean isValid() {
        var context = getContext();
        if (!context.containsKey(String.format(CONST_PRINCIPAL_FORMAT, CONST_DOCUMENT)))
            return false;

        if (!context.containsKey(String.format(CONST_PRINCIPAL_FORMAT, CONST_CUSTOMER_ID)))
            return false;


        if (getCustomerId() == null)
            return false;

        if (getDocument() == null || getDocument().isEmpty())
            return false;

        try {
            if (Long.parseLong(getDocument()) <= 0)
                return false;
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static synchronized String getDocument() {
        return getContext().get(String.format(CONST_PRINCIPAL_FORMAT, CONST_DOCUMENT));
    }

    public static synchronized UUID getCustomerId() {
        try {
            var value = getContext().get(String.format(CONST_PRINCIPAL_FORMAT, CONST_CUSTOMER_ID));
            return UUID.fromString(value);
        } catch (Exception e) {
            return null;
        }
    }

    public static synchronized void setToken(final Map<String, String> token) {
        var context = getContext();
        context.putAll(token);
    }
}
