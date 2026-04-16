package com.firestreak.user_service.dto;
import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record LoginRequest(@NotBlank(message = "Login/email can't be blank")
                           String identifier,
                           @NotBlank(message = "Password can't be blank")
                           @Size(min = 8, message = "Password must be longer than 8 chars")
                           String password) {}

