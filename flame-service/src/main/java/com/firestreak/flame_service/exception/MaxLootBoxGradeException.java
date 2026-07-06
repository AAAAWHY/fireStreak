package com.firestreak.flame_service.exception;

public class MaxLootBoxGradeException extends RuntimeException {
    public MaxLootBoxGradeException() {
        super("lootBox already has max lvl");
    }
}
