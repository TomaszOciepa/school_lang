package com.tom.courseservice.exception;

public enum CourseError {

    COURSE_NOT_FOUND("Course does not exists"),
    COURSE_NAME_ALREADY_EXISTS("Course name is already exists"),
    COURSE_IS_NOT_ACTIVE("Course is not ACTIVE"),
    STUDENT_IS_NOT_ACTIVE("Student is not ACTIVE"),
    STUDENT_IS_NOT_FOUND("Student is not Found"),
    STUDENT_ALREADY_ENROLLED("Student already enrolled on this course"),
    STUDENT_CANNOT_BE_ENROLL("Student cannot be enroll on course"),
    TEACHER_IS_NOT_ACTIVE("Teacher is not ACTIVE"),
    TEACHER_ALREADY_ENROLLED("Teacher already enrolled on this course");

    private String message;

    CourseError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
