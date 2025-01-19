package com.tom.activity_log_service.service;

import com.tom.activity_log_service.model.ActivityLogTeacher;

import java.util.List;

public interface ActivityLogTeacherService {

    void createLog(ActivityLogTeacher log);
    List<ActivityLogTeacher> getLogByTeacherId(String id);
    void deleteLogByTeacherId(String id);
    void dropAll();
}
