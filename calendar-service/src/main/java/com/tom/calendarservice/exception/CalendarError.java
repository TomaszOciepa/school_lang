package com.tom.calendarservice.exception;

public enum CalendarError {

    CALENDAR_NOT_FOUND("Calendar does not exists"),
    CALENDAR_LESSONS_NOT_FOUND("Lessons does not exists"),
    COURSE_BUSY_AT_TIME_SLOT("Course is busy at the specified time slot"),
    COURSE_STUDENTS_LIST_IS_EMPTY("Course student list is empty. You need to add students to the course"),

    TEACHER_LESSONS_LIST_IS_EMPTY("Teacher lessons list is empty"),
    TEACHER_BUSY_AT_TIME_SLOT("Teacher is busy at the specified time slot"),
    TEACHER_IS_NOT_ENROLLED_IN_COURSE("Teacher is not enrolled in course"),
    LESSON_START_DATE_IS_BEFORE_COURSE_START_DATE("Lesson start  date is before course start date");


    private String message;

    CalendarError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
