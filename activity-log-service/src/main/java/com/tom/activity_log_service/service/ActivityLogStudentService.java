package com.tom.activity_log_service.service;

import com.tom.activity_log_service.model.ActivityLogStudent;

import java.util.List;

public interface ActivityLogStudentService {

    void createLog(ActivityLogStudent log);
    List<ActivityLogStudent> getLogByStudentId(String id);
    void deleteLogByStudentId(String id);
    void dropAll();
}
