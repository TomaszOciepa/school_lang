package com.tom.calendarservice.exception;

public enum CalendarError {

    CALENDAR_NOT_FOUND("Calendar does not exists"),
    Teacher_NOT_FOUND_IN_COURSE("Teacher not found in course teacher list");

    private String message;

    CalendarError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
