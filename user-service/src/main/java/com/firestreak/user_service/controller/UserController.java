package com.firestreak.user_service.controller;


import com.firestreak.user_service.dto.*;
import com.firestreak.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    ChangePasswordResponse changePassword (
            @RequestBody @Valid ChangePasswordRequest request){
        return userService.changePassword(request);
    }
}
