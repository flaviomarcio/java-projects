package com.org.security.util;

import java.util.Arrays;

public class UrlHelper {

    private static final String TRUSTED_URLS = "%s/** /actuator/** /prometheus/**";
    private static String CONTEXT_PATH;

    private UrlHelper() {

        super();
    }

    public static String getContextPath() {
        return CONTEXT_PATH == null || CONTEXT_PATH.isEmpty()
                ? ""
                : CONTEXT_PATH;
    }

    public static void setContextPath(String context_path) {
        CONTEXT_PATH = context_path;
    }

    public static String[] getTrustedUrl() {
        return String.format(TRUSTED_URLS, getContextPath()).split(" ");
    }

    public static boolean isTrustedUrl(final String urlPath) {
        var trustedUrl = getTrustedUrl();
        return Arrays.stream(trustedUrl).anyMatch(urlPath::startsWith);
    }

}
