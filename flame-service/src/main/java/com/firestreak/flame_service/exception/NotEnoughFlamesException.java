package com.firestreak.flame_service.exception;

public class NotEnoughFlamesException extends RuntimeException {
    public NotEnoughFlamesException() {
        super("not enough flames");
    }
}
