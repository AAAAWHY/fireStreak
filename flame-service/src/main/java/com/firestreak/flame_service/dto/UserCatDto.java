package com.firestreak.flame_service.dto;

import lombok.Builder;

@Builder
public record UserCatDto(Long lvl,
                         Long productivity,
                         Long upgradeCost,
                         CatCardDto catCardDto) {
}
