package com.tom.calendarservice.exception;

public enum CalendarError {

        CALENDAR_NOT_FOUND("Calendar does not exists"),
    CALENDAR_LESSONS_NOT_FOUND("Lessons does not exists"),
    LESSON_START_DATE_IS_AFTER_END_DATE("Lesson start date is after lesson end date."),

   LESSON_LIMIT_REACHED_ERROR_MESSAGE("Lesson limit has been reached."),
    COURSE_BUSY_AT_TIME_SLOT("Course is busy at the specified time slot"),

    TEACHER_LESSONS_LIST_IS_EMPTY("Teacher lessons list is empty"),
    TEACHER_BUSY_AT_TIME_SLOT("Teacher is busy at the specified time slot"),
    TEACHER_IS_NOT_ENROLLED_IN_COURSE("Teacher is not enrolled in course"),
    LESSON_START_DATE_IS_BEFORE_COURSE_START_DATE("Lesson start date is before course start date"),
    LESSON_END_DATE_IS_BEFORE_COURSE_START_DATE("Lesson end date is before course start date"),
    LESSON_START_DATE_IS_AFTER_COURSE_END_DATE("Lesson start date is after course end date"),
    LESSON_END_DATE_IS_AFTER_COURSE_END_DATE("Lesson end date is after course end date"),
    STUDENT_ALREADY_ENROLLED("Student is already enrolled in the lesson");


    private String message;

    CalendarError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
