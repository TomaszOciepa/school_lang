package com.tom.calendarservice.exception;

public enum CalendarError {

    CALENDAR_NOT_FOUND("Calendar does not exists"),
    CALENDAR_LESSONS_NOT_FOUND("Lessons does not exists"),
    Teacher_NOT_FOUND_IN_COURSE("Teacher not found in course teacher list"),
    TEACHER_BUSY_AT_TIME_SLOT("Teacher is busy at the specified time slot");


    private String message;

    CalendarError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
