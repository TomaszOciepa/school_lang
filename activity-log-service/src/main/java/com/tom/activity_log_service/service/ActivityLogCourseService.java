package com.tom.activity_log_service.service;

import com.tom.activity_log_service.model.ActivityLogCourse;

import java.util.List;


public interface ActivityLogCourseService {

    void createLog(ActivityLogCourse log);
    List<ActivityLogCourse> getLogByCourseId(String id);
    void deleteLogByCourseId(String id);
    void dropAll();
}
