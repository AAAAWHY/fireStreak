package com.firestreak.flame_service.config;

import com.firestreak.flame_service.enums.Rarity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Map;

@ConfigurationProperties(prefix = "game")
@Component
@Getter
@Setter
public class GameConfig {
    private Map<Rarity, Double> productivityExp;
    private Map<Rarity, Double> upgradeCostExp;
    private Map<Rarity, Long> jumpLvl;
    private Map<Rarity, Double> jumpScale;

    private double lootBoxSigma;
    private double lootBoxMuMax;
    private int lootBoxGrades;
    private int lootBoxBaseCost;
    private double lootBoxGradeMultiplier;
}
