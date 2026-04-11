package com.firestreak.user_service.controller;


import com.firestreak.user_service.dto.*;
import com.firestreak.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;

    @PostMapping(path = "registration")
    RegisterResponse registration(@RequestBody @Valid RegisterRequest request){
        return userService.register(request);
    }

    @PostMapping(path = "login")
    LoginResponse login(@RequestBody @Valid LoginRequest request){
        return userService.login(request);
    }

    @PostMapping(path = "change-password")
    ChangePasswordResponse changePassword (@RequestBody @Valid ChangePasswordRequest request){
        Long id = 1L; //change later and get id from jwt
        return userService.changePassword(id, request);
    }
}
