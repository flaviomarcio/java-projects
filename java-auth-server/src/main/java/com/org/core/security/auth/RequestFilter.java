package com.org.core.security.auth;

import com.org.business.exceptions.InvalidInformationException;
import com.org.business.exceptions.UnAuthorizedException;
import com.org.business.exceptions.UserNotFoundException;
import com.org.core.helper.UrlHelper;
import com.org.core.utils.Singletons;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Nullable;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@Slf4j
public class RequestFilter extends OncePerRequestFilter {
    private final boolean debugMode;
    private final Singletons singletons;

    public RequestFilter(Environment environment, Singletons singletons) {
        this.singletons = singletons;
        this.debugMode = Boolean.parseBoolean(environment.getProperty("service.debug", "false"));
    }

    void logInfo(String message) {
        if (debugMode)
            log.info(message);
    }

    void logInfo(String format, Object arg1) {
        if (debugMode)
            log.info(format, arg1);
    }

    void logInfo(String format, Object arg1, Object arg2) {
        if (debugMode)
            log.info(format, arg1, arg2);
    }

    void logInfo(String format, Object arg1, Object arg2, Object arg3) {
        if (debugMode)
            log.info(format, arg1, arg2, arg3);
    }

    void logInfo(String format, Object arg1, Object arg2, Object arg3, Object arg4) {
        if (debugMode)
            log.info(format, arg1, arg2, arg3, arg4);
    }

    void logError(String message) {
        if (debugMode)
            log.error(message);
    }

    void logError(String format, Object arg1) {
        if (debugMode)
            log.error(format, arg1);
    }

    void logError(String format, Object arg1, Object arg2) {
        if (debugMode)
            log.error(format, arg1, arg2);
    }

    void logError(String format, Object arg1, Object arg2, Object arg3) {
        if (debugMode)
            log.error(format, arg1, arg2, arg3);
    }

    void logError(String format, Object arg1, Object arg2, Object arg3, Object arg4) {
        if (debugMode)
            log.error(format, arg1, arg2, arg3, arg4);
    }

    private void doFilter(String contextPath, @NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        if (UrlHelper.isTrustedOpenUrl(request.getRequestURI())) {
            logInfo("request: contextPath: {}, calling, openurl", contextPath);
            filterChain.doFilter(request, response);
            return;
        }

        var authentication = new RequestAuthentication(singletons);
        var credentials = String.valueOf(request.getHeader(HttpHeaders.AUTHORIZATION)).trim();
        logInfo("request: contextPath: {}, token checking", contextPath);
        var statusCode = authentication.parser(credentials);
        if (statusCode == HttpServletResponse.SC_OK || statusCode == HttpServletResponse.SC_CREATED || statusCode == HttpServletResponse.SC_ACCEPTED) {
            logInfo("request: contextPath: {}, calling, token is valid", contextPath);
            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);
            filterChain.doFilter(request, response);
            return;
        }

        response.setStatus(statusCode);
        logError("request: contextPath: {}, statusCode: {}, token checking fail", contextPath, statusCode);
    }

    @Override
    protected void doFilterInternal(@NotNull @Nullable HttpServletRequest request, @NotNull @Nullable HttpServletResponse response, @NotNull @Nullable FilterChain filterChain) throws ServletException, IOException {
        if (request == null || response == null) {
            logInfo("invalid request or response object");
            return;
        }
        var contextPath = request.getRequestURI().toLowerCase();
        try {
            logInfo("request: contextPath: {}, started", contextPath);
            this.doFilter(contextPath, request, response, filterChain);
        } catch (IOException | ServletException e) {
            logError("request: contextPath: {}, fail: {}", contextPath, e.getMessage());
            throw new RuntimeException(e);
        } catch (UnAuthorizedException e) {
            logError("request: contextPath: {}, fail: {}", contextPath, e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (UserNotFoundException e) {
            logError("request: contextPath: {}, fail: {}", contextPath, e.getMessage());
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (InvalidInformationException e) {
            logError("request: contextPath: {}, fail: {}", contextPath, e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } finally {
            logInfo("request: contextPath: {}, finished, statusCode: {}", contextPath, response.getStatus());
        }

    }

}