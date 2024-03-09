package com.org.tests.UT;

import com.org.business.dto.SignupIn;
import com.org.business.exceptions.UserNotFoundException;
import com.org.business.service.UserService;
import com.org.factory.FactoryByTests;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private final FactoryByTests factoryByTests = new FactoryByTests();
    private final UserService userService = factoryByTests.getSingletons().getUserService();

    @Test
    public void UIT_Check_register() {
        for (var userCheck : factoryByTests.getUserList())
            Assertions.assertNotNull(userService.register(SignupIn.from(userCheck)));
    }

    @Test
    public void UIT_Check_save() {
        for (var userCheck : factoryByTests.getUserList()) {
            Assertions.assertNotNull(userService.register(SignupIn.from(userCheck)));
            Assertions.assertNotNull(userService.createUser(userCheck));
        }
    }

    @Test
    public void UIT_Check_find() {
        factoryByTests.setupUserInject();
        for (var userCheck : factoryByTests.getUserList()) {
            var user = userService.findUser(userCheck.getId());
            Assertions.assertNotNull(user);
            Assertions.assertEquals(user.getId().toString(), userCheck.getId().toString());
            Assertions.assertEquals(user.getUsername(), userCheck.getUsername());
        }

        for (var userCheck : factoryByTests.getUserSaved()) {
            var user = userService.findUser(userCheck.getUsername());
            Assertions.assertNotNull(user);
            Assertions.assertEquals(user.getId().toString(), userCheck.getId().toString());
            Assertions.assertEquals(user.getUsername(), userCheck.getUsername());
            Assertions.assertEquals(user.getPasswordCrypt(), userCheck.getPasswordCrypt());
        }

        for (var userCheck : factoryByTests.getUserList()) {
            Assertions.assertTrue(userService.existsUser(userCheck.getUsername()));
            Assertions.assertTrue(userService.existsUser(userCheck.getId()));
            Assertions.assertNotNull(userService.deleteUser(userCheck.getId()));
            Assertions.assertNotNull(userService.deleteUser(userCheck.getUsername()));
            Assertions.assertNotNull(userService.updateUser(userCheck));
        }

        for (var userCheck : factoryByTests.getUserInvalidList()) {
            Assertions.assertFalse(userService.existsUser(userCheck.getUsername()));
            Assertions.assertFalse(userService.existsUser(userCheck.getId()));

            Assertions.assertThrows(UserNotFoundException.class, () -> userService.deleteUser(userCheck.getId()));
            Assertions.assertThrows(UserNotFoundException.class, () -> userService.deleteUser(userCheck.getUsername()));
            Assertions.assertThrows(UserNotFoundException.class, () -> userService.findUser(userCheck.getId()));
            Assertions.assertThrows(UserNotFoundException.class, () -> userService.findUser(userCheck.getUsername()));
        }

    }
}
