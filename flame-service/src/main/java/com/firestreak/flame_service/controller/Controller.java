package com.firestreak.flame_service.controller;

import com.firestreak.flame_service.dto.CatCardDto;
import com.firestreak.flame_service.dto.UserCatDto;
import com.firestreak.flame_service.dto.UserWalletDto;
import com.firestreak.flame_service.service.FlameService;
import com.firestreak.flame_service.service.LootBoxService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class Controller {
    private final FlameService flameService;
    private final LootBoxService lootBoxService;

    @GetMapping("/wallet")
    UserWalletDto getWallet(){
        return flameService.getWallet();
    }

    @GetMapping("/cats")
    List<UserCatDto> getUserCats(){
        return flameService.getUserCats();
    }

    @PatchMapping("/cats/{id}/upgrade")
    UserCatDto upgradeCat(@PathVariable("id") Long catId){
        return flameService.upgradeCat(catId);
    }

    @PostMapping("/lootBox/open")
    CatCardDto openLootBox(){
        return lootBoxService.openLootBox();
    }

    @PatchMapping("/lootBox/upgrade")
    Integer upgradeLootBox(){
        return lootBoxService.upgradeLootBox();
    }
}