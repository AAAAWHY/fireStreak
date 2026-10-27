package com.firestreak.flame_service.service;

import com.firestreak.flame_service.config.GameConfig;
import com.firestreak.flame_service.dto.CatCardDto;
import com.firestreak.flame_service.dto.UserCatDto;
import com.firestreak.flame_service.dto.UserWalletDto;
import com.firestreak.flame_service.entity.CatCard;
import com.firestreak.flame_service.entity.UserCat;
import com.firestreak.flame_service.entity.UserWallet;
import com.firestreak.flame_service.enums.Rarity;
import com.firestreak.flame_service.exception.CatNotFoundException;
import com.firestreak.flame_service.exception.NotEnoughPurrsException;
import com.firestreak.flame_service.exception.NotYourCatException;
import com.firestreak.flame_service.messaging.CreateWalletEvent;
import com.firestreak.flame_service.messaging.DailyStreakEvent;
import com.firestreak.flame_service.repository.CatCardRepository;
import com.firestreak.flame_service.repository.UserCatRepository;
import com.firestreak.flame_service.repository.UserWalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.Math.*;

@Service
@RequiredArgsConstructor
public class FlameService {
    private final CatCardRepository catCardRepository;
    private final UserCatRepository userCatRepository;
    private final UserWalletRepository userWalletRepository;
    private final GameConfig gameConfig;
    private final LootBoxService lootBoxService;



    public UserWalletDto getWallet(){
        long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserWallet wallet = userWalletRepository.findById(userId).get();
        return walletDtoMapper(wallet);
    }

    public void createWallet(CreateWalletEvent createWalletEvent){
        long userId = createWalletEvent.userId();
        UserWallet userWallet = UserWallet.builder().userId(userId).build();
        userWalletRepository.save(userWallet);
    }

    public void dailyWalletUpdate(DailyStreakEvent dailyStreakEvent){
        System.out.println("GOT EVENT: " + dailyStreakEvent);
        long userId = dailyStreakEvent.userId();
        int streak = dailyStreakEvent.streakCount();
        UserWallet wallet = userWalletRepository.findById(userId).get();

        if(streak == 0) {
            wallet.setCatsActive(false);
            wallet.setFlamesIncome(0D);
            userWalletRepository.save(wallet);
            return;
        }
        wallet.setCatsActive(true);
        wallet.setFlamesIncome((Math.log(1+streak) / Math.log(2)));
        wallet.setFlames(wallet.getFlames()+wallet.getFlamesIncome());
        wallet.setPurrs(wallet.getPurrs() + countPurrsIncome(userId));
        userWalletRepository.save(wallet);
    }

    public List<UserCatDto> getUserCats(){
        long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<UserCat> cats = userCatRepository.findAllByUserId(userId);
        List<Long> cardIds = cats.stream().map(UserCat::getCatCardId).distinct().toList();
        Map<Long, CatCard> cards = catCardRepository.findAllByIdIn(cardIds).stream()
                .collect(Collectors.toMap(CatCard::getId, catCard -> catCard));
        return cats.stream().map(cat-> catDtoMapper(cat, cards.get(cat.getCatCardId()))).toList();
    }

    public UserCatDto upgradeCat(Long catId){
        long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserCat cat = userCatRepository.findById(catId).orElseThrow(() -> new CatNotFoundException(catId));
        CatCard catCard = catCardRepository.findById(cat.getCatCardId()).get();
        if (!cat.getUserId().equals(userId)) throw new NotYourCatException(catId);
        UserWallet userWallet = userWalletRepository.findById(userId).get();
        long upgradeCost = countCatUpgradeCost(cat);

        if(userWallet.getPurrs() < upgradeCost) throw new NotEnoughPurrsException();
        userWallet.setPurrs(userWallet.getPurrs()-upgradeCost);
        cat.setLvl(cat.getLvl()+1);
        userCatRepository.save(cat);
        userWalletRepository.save(userWallet);
        return catDtoMapper(cat,catCard);
    }







    private Long countPurrsIncome(Long userId){
        List<UserCat> cats = userCatRepository.findAllByUserId(userId);
        List<Long> cardIds = cats.stream().map(UserCat::getCatCardId).distinct().toList();
        Map<Long, CatCard> cards = catCardRepository.findAllByIdIn(cardIds).stream().collect(Collectors.toMap(
                CatCard::getId,
                catCard -> catCard
        ));
        return cats.stream().mapToLong(cat->this.countCatProductivity(cat, cards.get(cat.getCatCardId()))).sum();
    }

    private Long countCatUpgradeCost(UserCat cat){
        CatCard catCard = catCardRepository.findById(cat.getCatCardId()).get();
        return countCatUpgradeCost(cat, catCard);
    }

    private Long countCatUpgradeCost(UserCat cat, CatCard catCard){
        long lvl = cat.getLvl();
        long baseUpgradeCost = catCard.getBaseUpgradeCost();
        Rarity rarity = catCard.getRarity();
        return (long) (baseUpgradeCost *
                pow(lvl, gameConfig.getUpgradeCostExp().get(rarity)) *
                pow(gameConfig.getJumpScale().get(rarity), max(0, lvl-gameConfig.getJumpLvl().get(rarity))));
    }

    private Long countCatProductivity(UserCat cat, CatCard catCard){
        long lvl = cat.getLvl();
        long baseProductivity = catCard.getBaseProductivity();
        Rarity rarity = catCard.getRarity();
        return (long) (baseProductivity *  pow(lvl, gameConfig.getProductivityExp().get(rarity)));
    }





    private CatCardDto catCardDtoMapper(CatCard catCard){
        return CatCardDto.builder()
                .name(catCard.getName())
                .baseProductivity(catCard.getBaseProductivity())
                .spriteName(catCard.getSpriteName())
                .animationName(catCard.getAnimationName())
                .rarity(catCard.getRarity())
                .build();
    }
    private UserCatDto catDtoMapper(UserCat cat, CatCard catCard){
        //CatCard catCard = catCardRepository.findById(cat.getCatCardId()).get();
        CatCardDto catCardDto = catCardDtoMapper(catCard);
        long productivity = countCatProductivity(cat, catCard);
        long upgradeCost = countCatUpgradeCost(cat, catCard);

        return UserCatDto.builder()
                .catCardDto(catCardDto)
                .lvl(cat.getLvl())
                .productivity(productivity)
                .upgradeCost(upgradeCost)
                .build();
    }
    private UserWalletDto walletDtoMapper(UserWallet wallet){
        Long dailyIncome = countPurrsIncome(wallet.getUserId());

        return UserWalletDto.builder()
                .purrs(wallet.getPurrs())
                .flames(wallet.getFlames())
                .dailyPurrsIncome(dailyIncome)
                .dailyFlamesIncome(wallet.getFlamesIncome())
                .lootBoxGrade(wallet.getLootBoxGrade())
                .catsActive(wallet.getCatsActive())
                .lootBoxUpgradeCost(lootBoxService.getUpgradeCost(wallet.getLootBoxGrade()))
                .build();
    }
}
