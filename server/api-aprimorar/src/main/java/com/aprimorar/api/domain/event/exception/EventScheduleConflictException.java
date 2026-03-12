package com.aprimorar.api.domain.event.exception;

public class EventScheduleConflictException extends RuntimeException {
  public EventScheduleConflictException(String message) {
    super(message);
  }
}
