package com.org.business.service;

import com.org.business.dto.*;
import com.org.business.exceptions.InvalidInformationException;
import com.org.business.exceptions.InvalidObjectException;
import com.org.business.exceptions.UnAuthorizedException;
import com.org.business.model.GrantCode;
import com.org.business.model.Token;
import com.org.business.model.User;
import com.org.business.repository.GrantCodeRepository;
import com.org.business.repository.TokenRepository;
import com.org.core.helper.MsgHelper;
import com.org.core.security.auth.RequestToken;
import com.org.core.utils.HashUtil;
import com.org.core.utils.PasswordUtil;
import com.org.core.utils.Singletons;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final Singletons singletons;

    private GrantCodeRepository grantCodeRepository() {
        return singletons.getRepositoryService().getGrantCodeRepository();
    }

    private TokenRepository tokenRepository() {
        return singletons.getRepositoryService().getTokenRepository();
    }

    public GrantCodeOut grantCode(GrantCodeIn grantCodeIn) {
        if (grantCodeIn == null)
            throw InvalidInformationException.e(MsgHelper.INVALID_INFORMATION);
        User user = singletons.getUserService().findUser(grantCodeIn.getAuthMode(), grantCodeIn.getClientId());
        if (user == null)
            throw UnAuthorizedException.e(MsgHelper.USER_NO_EXISTS, grantCodeIn.getClientId());

        var grantCode = GrantCode.builder()
                .id(UUID.randomUUID())
                .dt(LocalDateTime.now())
                .scopeId(user.getScopeId())
                .userId(user.getId())
                .expires(LocalDateTime.now().plusDays(365))
                .enabled(true)
                .build();

        grantCodeRepository().save(grantCode);

        return GrantCodeOut
                .builder()
                .code(grantCode.getId())
                .build();
    }

    private TokenOut accessToken(User user) {
        if (user == null || !user.isEnabled())
            throw InvalidObjectException.e(MsgHelper.USER_INVALID_OBJECT);
        var authentication = UsernamePasswordAuthenticationToken.authenticated(user, null, null);
        var tokenResponse = singletons.getTokenGenerator().createToken(singletons.getScopeService().getScopeSystem(), authentication);
        var token = Token.from(user, tokenResponse);
        tokenRepository().save(token);
        return tokenResponse;
    }

    public TokenOut accessToken(AccessTokenIn accessTokenIn) {
        var grantCode = grantCodeRepository().findById(accessTokenIn.getCode()).orElse(null);
        if (grantCode == null || !grantCode.isEnabled() || grantCode.isExpired())
            throw InvalidObjectException.e(MsgHelper.OBJECT_INVALID);
        return this.accessToken(singletons.getUserService().findUser(grantCode.getUserId().toString()));
    }

    public User tokenVerify(RequestToken requestToken) {
        if (requestToken == null || requestToken.getToken() == null || requestToken.getToken().trim().isEmpty())
            throw UnAuthorizedException.e(MsgHelper.TOKEN_INVALID);

        var tokenUuid = HashUtil.toMd5Uuid(requestToken.getToken());

        var token = tokenRepository().findByAccessTokenMd5(tokenUuid).orElse(null);
        if (token == null || !token.isEnabled() || token.isExpired())
            throw UnAuthorizedException.e(MsgHelper.TOKEN_EXPIRED);

        var user = singletons.getUserService().findUser(token.getUserId());
        if (user == null || !user.isEnabled())
            throw UnAuthorizedException.e(MsgHelper.USER_NO_EXISTS);

        return user;
    }

    public UserSessionOut getSession(UUID userId) {
        var user = singletons.getUserService().findUser(userId);
        if (user == null)
            throw UnAuthorizedException.e("session: User not found: clienteId: null");
        return UserSessionOut.from(user);
    }

    public User passwordVerify(LoginIn loginIn) {
        var user = singletons.getUserService().findUser(loginIn.getClientId());
        if (user == null)
            return null;

        if (!PasswordUtil.verify(loginIn.getSecret(), user.getPasswordCrypt()))
            return null;

        return user;
    }

    public UserSessionOut login(LoginIn loginIn) {
        var user = passwordVerify(loginIn);
        if (user == null)
            throw UnAuthorizedException.e("User unauthorized: clienteId: {}", loginIn.getClientId());

        var accessToken = this.accessToken(user);
        return UserSessionOut.from(user, accessToken);
    }

    public TokenOut refreshToken(TokenOut tokenOut) {
        var tokenGenerator = singletons.getTokenGenerator();
        Authentication authentication = singletons.getJwtRefreshTokenAuthProvider().authenticate(new BearerTokenAuthenticationToken(tokenOut.getRefreshToken()));
        if (!authentication.isAuthenticated())
            throw UnAuthorizedException.e(MsgHelper.TOKEN_EXPIRED);
        return tokenGenerator.createToken(singletons.getScopeService().getScopeSystem(), authentication);
    }

    public UserOut changePassword(UUID userId, UserChangePassword changePassword) {
        var user = singletons.getUserService().findUser(userId);
        if (user == null)
            throw UnAuthorizedException.e(MsgHelper.USER_INVALID_OBJECT);
        user.setPasswordCrypt(changePassword.getPassword());
        singletons.getUserService().encryptPassword(user);
        singletons.getUserService().updateUser(user);
        return UserOut.from(user);
    }

    public UserOut resetPassword(UserResetPassword resetPassword) {
        var user = singletons.getUserService().findUser(resetPassword.getClientId());
        String newPassword = HashUtil.toMd5(UUID.randomUUID().toString());
        newPassword = newPassword.substring(0, 6).toUpperCase();

        var userResponse = changePassword(
                user.getId(),
                UserChangePassword
                        .builder()
                        .password(newPassword)
                        .build()
        );

        singletons.getDispatcherService().userDispatcher(user, resetPassword.getDispatcher(), MsgHelper.o(MsgHelper.USER_YOUR_NEW_PASSWORD, newPassword));
        return userResponse;
    }

}
