package com.tom.calendarservice.exception;



public class CalendarException extends RuntimeException{

   private CalendarError calendarError;

    public CalendarException(CalendarError calendarError) {
        this.calendarError = calendarError;
    }

    public CalendarError getCalendarError() {
        return calendarError;
    }
}
