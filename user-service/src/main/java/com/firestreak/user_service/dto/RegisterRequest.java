package com.firestreak.user_service.dto;
import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record RegisterRequest(@NotBlank(message = "Login can't be blank")
                              String login,
                              @Email(message = "Incorrect email")
                              String email,
                              @NotBlank(message = "Password can't be blank")
                              @Size(min = 8, message = "Password must be longer than 8 chars")
                              String password,
                              @NotBlank(message = "Username can't be blank")
                              String username) {}
