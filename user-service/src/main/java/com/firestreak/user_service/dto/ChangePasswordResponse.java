package com.firestreak.user_service.dto;
import lombok.Builder;

@Builder
public record ChangePasswordResponse(boolean success, String error) {}
