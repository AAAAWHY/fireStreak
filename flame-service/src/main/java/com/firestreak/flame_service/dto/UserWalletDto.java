package com.firestreak.flame_service.dto;

import lombok.Builder;

@Builder
public record UserWalletDto(Double flames,
                            Long purrs,
                            Integer lootBoxGrade,
                            Boolean catsActive,
                            Long dailyPurrsIncome,
                            Double dailyFlamesIncome,
                            Integer lootBoxUpgradeCost) {
}
