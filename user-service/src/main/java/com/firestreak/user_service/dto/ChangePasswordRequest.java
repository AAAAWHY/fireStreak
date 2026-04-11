package com.firestreak.user_service.dto;
import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record ChangePasswordRequest(@NotBlank(message = "Password can't be blank")
                                    @Size(min = 8, message = "Password must be longer than 8 chars")
                                    String curPassword,
                                    @NotBlank(message = "Password can't be blank")
                                    @Size(min = 8, message = "Password must be longer than 8 chars")
                                    String newPassword) {}
