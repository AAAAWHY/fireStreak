package com.firestreak.user_service.controller;


import com.firestreak.user_service.dto.*;
import com.firestreak.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;

    @PostMapping(path = "registration")
    ResponseEntity<String> registration(@RequestBody @Valid RegisterRequest request){
        return ResponseEntity.ok(userService.register(request));
    }

    @PostMapping(path = "login")
    ResponseEntity<String> login(@RequestBody @Valid LoginRequest request){
        return ResponseEntity.ok(userService.login(request));
    }

    @PostMapping(path = "change-password")
    ResponseEntity<Void> changePassword (
            @RequestBody @Valid ChangePasswordRequest request){
        userService.changePassword(request);
        return ResponseEntity.noContent().build();
    }
}