package com.tom.courseservice.exception;

public enum CourseError {

    COURSE_NOT_FOUND("Course does not exists"),
    COURSE_IS_FULL("Course is full"),
    COURSE_IS_NOT_FULL("Course is not full"),
    COURSE_STUDENT_LIST_IS_EMPTY("Course student list is empty"),
    COURSE_START_DATE_IS_AFTER_END_DATE("Course start date is after end date"),
    COURSE_END_DATE_IS_BEFORE_START_DATE("Course end date is before start date"),
    COURSE_PARTICIPANTS_NUMBER_IS_BIGGER_THEN_PARTICIPANTS_LIMIT("Course participants number is bigger then participants limit"),
    COURSE_LESSONS_NUMBER_IS_BIGGER_THEN_LESSONS_LIMIT("Course lessons number is bigger then lessons limit"),
    COURSE_LESSONS_FINISHED_IS_BIGGER_THEN_LESSONS_NUMBER("Course lessons finished is bigger then lessons number"),
    COURSE_TEACHER_LIST_IS_EMPTY("Course teacher list is empty"),
    COURSE_NAME_ALREADY_EXISTS("Course name is already exists"),
    COURSE_IS_NOT_ACTIVE("Course is not ACTIVE"),

    COURSE_LESSON_LIMIT_REACHED("Course lesson limit reached"),
    STUDENT_OPERATION_FORBIDDEN("Operation is forbidden."),
    STUDENT_IS_NOT_ACTIVE("Student is not ACTIVE"),
    STUDENT_IS_ACTIVE("Student is ACTIVE"),
    STUDENT_IS_NOT_FOUND("Student is not Found"),
    STUDENT_NO_ON_THE_LIST_OF_ENROLL("No student on the list of enroll"),
    STUDENT_ALREADY_ENROLLED("Student already enrolled on this course"),
    STUDENT_CANNOT_BE_ENROLL("Student cannot be enroll on course"),
    TEACHER_IS_NOT_ACTIVE("Teacher is not ACTIVE"),

    TEACHER_HAS_LESSONS_IN_COURSE("Teacher has lessons in course"),
    TEACHER_NO_ON_THE_LIST_OF_ENROLL("No teacher on the list of enroll"),

    TEACHER_ALREADY_ENROLLED("Teacher already enrolled on this course");

    private String message;

    CourseError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
