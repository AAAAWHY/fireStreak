package com.firestreak.flame_service.service;

import com.firestreak.flame_service.config.GameConfig;
import com.firestreak.flame_service.dto.CatCardDto;
import com.firestreak.flame_service.entity.CatCard;
import com.firestreak.flame_service.entity.UserCat;
import com.firestreak.flame_service.entity.UserWallet;
import com.firestreak.flame_service.enums.Rarity;
import com.firestreak.flame_service.exception.MaxLootBoxGradeException;
import com.firestreak.flame_service.exception.NotEnoughFlamesException;
import com.firestreak.flame_service.exception.NotEnoughPurrsException;
import com.firestreak.flame_service.repository.CatCardRepository;
import com.firestreak.flame_service.repository.UserCatRepository;
import com.firestreak.flame_service.repository.UserWalletRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class LootBoxService {
    private final CatCardRepository catCardRepository;
    private final UserWalletRepository userWalletRepository;
    private final UserCatRepository userCatRepository;
    private final GameConfig gameConfig;

    private static final Rarity[] RARITIES = Rarity.values();

    private double sigma;
    private double muMax;
    private int grades;
    private int baseCost;
    private double gradeMultiplier;

    @PostConstruct
    void init() {
        sigma = gameConfig.getLootBoxSigma();
        muMax = gameConfig.getLootBoxMuMax();
        grades = gameConfig.getLootBoxGrades();
        baseCost = gameConfig.getLootBoxBaseCost();
        gradeMultiplier = gameConfig.getLootBoxGradeMultiplier();
    }

    private double erf(double x) {
        double t = 1 / (1 + 0.3275911 * Math.abs(x));
        double y = 1 - ((((1.061405429 * t - 1.453152027) * t
                + 1.421413741) * t - 0.284496736) * t
                + 0.254829592) * t * Math.exp(-x * x);
        return x >= 0 ? y : -y;
    }

    private double cdf(double x) {
        return 0.5 * (1 + erf(x / Math.sqrt(2)));
    }

    private double mu(int grade) {
        return 1 + (double)(grade - 1) / (grades - 1) * (muMax - 1);
    }

    private double[] getWeights(int grade) {
        double mu  = mu(grade);
        double[] w = new double[RARITIES.length];
        for (int i = 0; i < w.length; i++) {
            double lo = i == 0            ? Double.NEGATIVE_INFINITY : (i + 1) - 0.5;
            double hi = i == w.length - 1 ? Double.POSITIVE_INFINITY : (i + 1) + 0.5;
            double pLo = lo == Double.NEGATIVE_INFINITY ? 0 : cdf((lo - mu) / sigma);
            double pHi = hi == Double.POSITIVE_INFINITY ? 1 : cdf((hi - mu) / sigma);
            w[i] = Math.max(0, pHi - pLo);
        }
        return w;
    }

    private Rarity roll(int grade) {
        double[] w    = getWeights(grade);
        double   roll = ThreadLocalRandom.current().nextDouble();
        for (int i = 0; i < w.length; i++) {
            roll -= w[i];
            if (roll <= 0) return RARITIES[i];
        }
        return RARITIES[RARITIES.length - 1];
    }




    public CatCardDto openLootBox(){
        long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserWallet wallet = userWalletRepository.findById(userId).get();
        if(wallet.getFlames()<1)
            throw new NotEnoughFlamesException();

        wallet.setFlames(wallet.getFlames()-1);

        Rarity rarity = roll(wallet.getLootBoxGrade());
        List<CatCard> catCards = catCardRepository.findAllByRarity(rarity);
        CatCard catCard = catCards.get(ThreadLocalRandom.current().nextInt(catCards.size()));

        UserCat userCat = UserCat.builder()
                .userId(userId)
                .catCardId(catCard.getId())
                .build();

        userCatRepository.save(userCat);
        userWalletRepository.save(wallet);

        return CatCardDto.builder()
                .name(catCard.getName())
                .rarity(catCard.getRarity())
                .baseProductivity(catCard.getBaseProductivity())
                .animationName(catCard.getAnimationName())
                .spriteName(catCard.getSpriteName())
                .build();
    }

    int getUpgradeCost(int grade){
        return (int) (baseCost * Math.pow(gradeMultiplier, grade-1));
    }


    public Integer upgradeLootBox(){
        long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserWallet wallet = userWalletRepository.findById(userId).get();
        long cost = getUpgradeCost(wallet.getLootBoxGrade());

        if(wallet.getLootBoxGrade()==grades)
            throw new MaxLootBoxGradeException();
        if(wallet.getPurrs()<cost)
            throw new NotEnoughPurrsException();

        wallet.setPurrs(wallet.getPurrs()-cost);
        wallet.setLootBoxGrade(wallet.getLootBoxGrade()+1);
        userWalletRepository.save(wallet);
        return wallet.getLootBoxGrade();
    }
}
