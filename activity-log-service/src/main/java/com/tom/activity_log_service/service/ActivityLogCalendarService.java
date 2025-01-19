package com.tom.activity_log_service.service;


import com.tom.activity_log_service.model.ActivityLogCalendar;

import java.util.List;

public interface ActivityLogCalendarService {

    void createLog(ActivityLogCalendar log);
    List<ActivityLogCalendar> getLogByCalendarId(String id);
    void deleteLogByCalendarId(String id);
    void dropAll();

}
