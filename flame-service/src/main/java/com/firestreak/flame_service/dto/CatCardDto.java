package com.firestreak.flame_service.dto;

import com.firestreak.flame_service.entity.UserCat;
import com.firestreak.flame_service.enums.Rarity;
import lombok.Builder;

@Builder
public record CatCardDto(Long baseProductivity,
                         String name,
                         Rarity rarity,
                         String spriteName,
                         String animationName) {
}
