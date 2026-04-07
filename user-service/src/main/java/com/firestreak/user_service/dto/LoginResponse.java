package com.firestreak.user_service.dto;
import lombok.Builder;

@Builder
public record LoginResponse(boolean success, String jwt, String error) {}
