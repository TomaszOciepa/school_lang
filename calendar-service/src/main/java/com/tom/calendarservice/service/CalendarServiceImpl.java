package com.tom.calendarservice.service;

import com.tom.calendarservice.exception.CalendarError;
import com.tom.calendarservice.exception.CalendarException;
import com.tom.calendarservice.model.Calendar;
import com.tom.calendarservice.repo.CalendarRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CalendarServiceImpl implements CalendarService {

    private CalendarRepository calendarRepository;

    @Override
    public List<Calendar> getAllLessons() {
        return calendarRepository.findAll();
    }

    @Override
    public Calendar getLessonById(String id) {
        return  calendarRepository.findById(id)
                .orElseThrow(()-> new CalendarException(CalendarError.CALENDAR_NOT_FOUND));
    }

    @Override
    public Calendar addLesson(Calendar calendar) {
        return calendarRepository.save(calendar);
    }

    @Override
    public Calendar putLesson(String id, Calendar calendar) {
        return calendarRepository.findById(id)
                .map(calendarFromDb ->{
                  calendarFromDb.setEventName(calendar.getEventName());
                  calendarFromDb.setStartDate(calendar.getStartDate());
                  calendarFromDb.setEndDate(calendar.getEndDate());
                  calendarFromDb.setStudentId(calendar.getStudentId());
                  calendarFromDb.setTeacherId(calendar.getTeacherId());
                  calendarFromDb.setCourseId(calendar.getCourseId());
                  calendarFromDb.setDescription(calendar.getDescription());
                  return calendarRepository.save(calendarFromDb);
                })
                .orElseThrow(()-> new CalendarException(CalendarError.CALENDAR_NOT_FOUND));
    }

    @Override
    public Calendar patchLesson(String id, Calendar calendar) {
        Calendar calendarFromDB = calendarRepository.findById(id)
                .orElseThrow(()-> new CalendarException(CalendarError.CALENDAR_NOT_FOUND));

        if(calendar.getEventName() != null){
            calendarFromDB.setEventName(calendar.getEventName());
        }
        if(calendar.getStartDate() != null){
            calendarFromDB.setStartDate(calendar.getStartDate());
        }
        if (calendar.getEndDate() != null){
            calendarFromDB.setEndDate(calendar.getEndDate());
        }
        if(calendar.getStudentId() != null){
            calendarFromDB.setStudentId(calendar.getStudentId());
        }
        if(calendar.getTeacherId() != null){
            calendarFromDB.setTeacherId(calendar.getTeacherId());
        }
        if(calendar.getCourseId() != null){
            calendarFromDB.setCourseId(calendar.getCourseId());
        }
        if(calendar.getDescription() != null){
            calendarFromDB.setDescription(calendar.getDescription());
        }

        return calendarRepository.save(calendarFromDB);
    }

    @Override
    public void deleteLesson(String id) {
      calendarRepository.findById(id)
                .orElseThrow(() -> new CalendarException(CalendarError.CALENDAR_NOT_FOUND));
      calendarRepository.deleteById(id);
    }
}
