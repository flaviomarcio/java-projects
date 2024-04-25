package com.org.business.adapters;

import com.littlecode.parsers.HashUtil;
import com.org.business.dto.*;
import com.org.business.service.AuthenticationService;
import com.org.commons.security.auth.ContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/grant-code")
    public ResponseEntity<GrantCodeOut> grantCode(@RequestBody GrantCodeIn grantCodeIn) {
        var out = authenticationService.grantCode(grantCodeIn);
        if (out == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.ok(out);
    }

    @PostMapping(value = {"/login"})
    public ResponseEntity<UserSessionOut> login(@RequestBody LoginIn loginIn) {
        var out = authenticationService.login(loginIn);
        if (out == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.ok(out);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout-all")
    public ResponseEntity<Void> logoutAll() {
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = {"/access-token"})
    public ResponseEntity<TokenOut> accessToken(@RequestParam("code") String code, @RequestParam("grant_type") String grantType) {
        var accessTokenRequest = AccessTokenIn.builder()
                .code(HashUtil.toUuid(code))
                .grantType(grantType)
                .build();
        var out = authenticationService.accessToken(accessTokenRequest);
        if (out == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.ok(out);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenOut> refreshToken(@RequestBody TokenOut tokenOut) {
        return ResponseEntity.ok(authenticationService.refreshToken(tokenOut));
    }

    @PostMapping("/change-password")
    public ResponseEntity<UserOut> changePassword(@RequestBody UserChangePassword userChangePassword) {
        return ResponseEntity.ok(authenticationService.changePassword(ContextHolder.getUserId(), userChangePassword));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<UserOut> resetPassword(@RequestBody UserResetPassword resetPassword) {
        return ResponseEntity.ok(authenticationService.resetPassword(resetPassword));
    }

    @GetMapping("/check")
    public ResponseEntity<UserSessionOut> check() {
        return ResponseEntity.ok(authenticationService.getSession(ContextHolder.getUserId()));
    }

    @GetMapping("/session")
    public ResponseEntity<Object> session() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/sessions")
    public ResponseEntity<List<Object>> sessions() {
        return ResponseEntity.ok().build();
    }



}
