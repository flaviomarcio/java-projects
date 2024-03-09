package com.org.tests.UT;

import com.org.business.dto.LoginIn;
import com.org.business.dto.UserSessionOut;
import com.org.business.service.AuthenticationService;
import com.org.factory.FactoryByTests;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {
    private final FactoryByTests factoryByTests = new FactoryByTests();
    private final AuthenticationService authenticationService = factoryByTests.getSingletons().getAuthenticationService();

    @BeforeEach
    public void setUp() {
        factoryByTests.setupUserInject();
    }

    @Test
    public void UIT_Check() {

        List<UserSessionOut> sessionResponseList = new ArrayList<>();
        for (var userCheck : factoryByTests.getUserList()) {
            var tokenResponse = authenticationService.login(LoginIn.from(userCheck));
            Assertions.assertNotNull(tokenResponse);
            sessionResponseList.add(tokenResponse);
        }

        for (var sessionResponse : sessionResponseList) {
            Assertions.assertNotNull(authenticationService.refreshToken(sessionResponse.getToken()));
        }
    }


}
