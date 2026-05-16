package com.firestreak.flame_service.exception;

public class CatNotFoundException extends RuntimeException {
    public CatNotFoundException(Long catId) {
        super("cat with id "+catId+" doesn't exist");
    }
}
