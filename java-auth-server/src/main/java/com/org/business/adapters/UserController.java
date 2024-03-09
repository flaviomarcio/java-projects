package com.org.business.adapters;

import com.org.business.dto.SignupIn;
import com.org.business.dto.UserOut;
import com.org.business.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserOut> register(@RequestBody SignupIn signupIn) {
        var out = userService.register(signupIn);
        if (out == null)
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        return ResponseEntity.ok().body(out);
    }

    @GetMapping("/find")
    public ResponseEntity<UserOut> find(String userKey) {
        var outUser = userService.findUser(userKey);
        return outUser == null
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.ok(UserOut.from(outUser));
    }

    @GetMapping("/delete")
    public ResponseEntity<Void> delete(String userKey) {
        userService.deleteUser(userKey);
        return ResponseEntity.ok().build();
    }
}
