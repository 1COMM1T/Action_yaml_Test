package com.commit.campus.controller;

import com.commit.campus.dto.SignUpUserRequest;
import com.commit.campus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("")
    public ResponseEntity<Void> signUpUser(@RequestBody SignUpUserRequest userRequest) {
        userService.signUpUser(userRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/withdraw/{userId}")
    public ResponseEntity<Void> withdrawUser (@PathVariable("userId") Long userId) {
        userService.withdrawUser(userId);
        return ResponseEntity.ok().build();
    }

}
