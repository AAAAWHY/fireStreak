package com.firestreak.user_service.dto;
import lombok.Builder;

@Builder
public record RegisterResponse(boolean success, String jwt, String error) {}
