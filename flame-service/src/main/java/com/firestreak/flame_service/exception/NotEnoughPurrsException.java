package com.firestreak.flame_service.exception;

public class NotEnoughPurrsException extends RuntimeException {
    public NotEnoughPurrsException() {
        super("Not enough purrs");
    }
}
