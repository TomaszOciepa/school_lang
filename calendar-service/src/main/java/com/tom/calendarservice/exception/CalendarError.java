package com.tom.calendarservice.exception;

public enum CalendarError {

    CALENDAR_NOT_FOUND("Calendar does not exists");

    private String message;

    CalendarError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
