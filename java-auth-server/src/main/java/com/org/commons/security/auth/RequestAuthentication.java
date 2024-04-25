package com.org.commons.security.auth;

//import com.org.business.dto.LoginIn;
//import com.org.business.dto.UserContextHolder;
//import com.org.business.exceptions.AuthGenericException;
//import com.org.business.model.User;
//import com.org.core.helper.MsgHelper;
//import com.org.core.security.JwtParserUtil;
//import com.org.core.utils.Singletons;
//import lombok.Getter;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.authentication.AbstractAuthenticationToken;
//
//import javax.servlet.http.HttpServletResponse;
//import java.util.Base64;
//import java.util.List;

//@Slf4j
public class RequestAuthentication /*extends AbstractAuthenticationToken */{
//    public static final String BASIC = "Basic";
//    public static final String BEARER = "Bearer";
//    private final Singletons SINGLETON;
//
//    @Getter
//    private String message;
//
//    public RequestAuthentication(Singletons singletons) {
//        super(null);
//        setAuthenticated(false);
//
//        SINGLETON = singletons;
//        if (SINGLETON == null)
//            throw AuthGenericException.e(MsgHelper.o(MsgHelper.OBJECT_INVALID, Singletons.class));
//    }
//
//    public int parser(String credentialsEncoded) {
//
//        if (credentialsEncoded == null || credentialsEncoded.isEmpty() || credentialsEncoded.equals("null"))
//            return HttpServletResponse.SC_UNAUTHORIZED;
//        var credentials = List.of(credentialsEncoded.split(" "));
//        if (credentials.size() != 2) {
//            this.message = MsgHelper.o(MsgHelper.INVALID_DATA, "parser credentials.size()");
//            return HttpServletResponse.SC_BAD_REQUEST;
//        }
//
//        var tokenType = credentials.get(0);
//
//        if (tokenType.equals(BASIC))
//            return this.parserBasic(new String(Base64.getDecoder().decode(credentials.get(1))));
//
//        if (tokenType.equals(BEARER))
//            return this.parserBearer(credentials.get(1));
//
//        this.message = MsgHelper.o(MsgHelper.INVALID_DATA, "unknown tokenType " + tokenType);
//        return HttpServletResponse.SC_NOT_ACCEPTABLE;
//
//    }
//
//    private int parserBasic(String credentialsDecoded) {
//
//        if (credentialsDecoded == null) {
//            log.warn(MsgHelper.INVALID_CREDENTIAL);
//            return HttpServletResponse.SC_UNAUTHORIZED;
//        }
//
//        List<String> credentials = List.of(credentialsDecoded.split(":"));
//        if (credentials.size() != 2) {
//            this.message = MsgHelper.o(MsgHelper.INVALID_DATA, "basic credentials.size()");
//            return HttpServletResponse.SC_BAD_REQUEST;
//        }
//
//        var username = credentials.get(0);
//        var password = credentials.get(1);
//        if (username == null || password == null || username.isEmpty() || password.isEmpty())
//            return HttpServletResponse.SC_UNAUTHORIZED;
//
//        var user = SINGLETON.getAuthenticationService().passwordVerify(LoginIn.builder().clientId(username).secret(password).build());
//        if (user == null)
//            return HttpServletResponse.SC_UNAUTHORIZED;
//
//        this.setAuthenticated(true);
//        ContextHolder.setPrincipal(user);
//        return HttpServletResponse.SC_OK;
//    }
//
//    private int parserBearer(String credentials) {
//
//        var requestToken = JwtParserUtil.parser(credentials);
//
//        if (requestToken == null || requestToken.isExpired()) {
//            log.warn(requestToken == null
//                    ? MsgHelper.o(MsgHelper.OBJECT_INVALID, RequestToken.class)
//                    : MsgHelper.TOKEN_EXPIRED);
//            return HttpServletResponse.SC_UNAUTHORIZED;
//        }
//
//        User userSession = SINGLETON.getAuthenticationService().tokenVerify(requestToken);
//        if (userSession == null)
//            return HttpServletResponse.SC_UNAUTHORIZED;
//        ContextHolder.setToken(requestToken.getPayload());
//        ContextHolder.setPrincipal(UserContextHolder.from(userSession));
//        this.setAuthenticated(ContextHolder.isValid());
//        return this.isAuthenticated()
//                ? HttpServletResponse.SC_OK
//                : HttpServletResponse.SC_UNAUTHORIZED;
//    }
//
//    @Override
//    public Object getCredentials() {
//        return ContextHolder.getCredential();
//    }
//
//    @Override
//    public Object getPrincipal() {
//        return ContextHolder.getPrincipal();
//    }
}
