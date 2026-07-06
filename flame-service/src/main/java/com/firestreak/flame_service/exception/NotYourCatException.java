package com.firestreak.flame_service.exception;

public class NotYourCatException extends RuntimeException {
  public NotYourCatException(Long catId) {
    super("user doesn't have cat with id "+catId);
  }
}
