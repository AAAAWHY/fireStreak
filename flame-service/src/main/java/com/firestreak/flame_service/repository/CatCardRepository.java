package com.firestreak.flame_service.repository;

import com.firestreak.flame_service.entity.CatCard;
import com.firestreak.flame_service.enums.Rarity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatCardRepository extends JpaRepository<CatCard, Long> {
    List<CatCard> findAllByRarity(Rarity rarity);
    List<CatCard> findAllByIdIn(List<Long> userIds);
}
