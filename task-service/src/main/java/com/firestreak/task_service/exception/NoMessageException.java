package com.firestreak.task_service.exception;

public class NoMessageException extends RuntimeException {
  public NoMessageException() {super("need message to verify");}
}
