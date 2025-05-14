package com.tom.salary_service.service;

import com.tom.salary_service.model.Salary;
import com.tom.salary_service.model.Status;
import com.tom.salary_service.model.dto.CalendarDto;
import com.tom.salary_service.repo.SalaryRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SalaryServiceImpl implements SalaryService {

    private final CalendarServiceClient calendarServiceClient;
    private final SalaryRepository salaryRepo;

    public SalaryServiceImpl(CalendarServiceClient calendarServiceClient, SalaryRepository salaryRepo) {
        this.calendarServiceClient = calendarServiceClient;
        this.salaryRepo = salaryRepo;
    }


    @Override
    public List<Salary> getSalaryByTeacherId(Long teacherId) {
        createSalary(teacherId);

        return salaryRepo.findByTeacherIdOrderByDateAsc(teacherId);
    }

    @Override
    public Salary getSalaryById(String id) {
        return salaryRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Salary not found with id: " + id));
    }


    private void createSalary(Long teacherId) {
        List<Salary> salaryList = salaryRepo.findByTeacherIdOrderByDateAsc(teacherId);
        List<CalendarDto> lessonsList = calendarServiceClient.getLessonByTeacherId(teacherId);

        Map<YearMonth, List<CalendarDto>> lessonsByMonth = groupLessonsByMonth(lessonsList);
        YearMonth currentMonth = YearMonth.now();

        for (Map.Entry<YearMonth, List<CalendarDto>> entry : lessonsByMonth.entrySet()) {
            YearMonth yearMonth = entry.getKey();
            List<CalendarDto> monthlyLessons = entry.getValue();

            Optional<Salary> existingSalaryOpt = findExistingSalary(salaryList, yearMonth);

            if (existingSalaryOpt.isPresent()) {
                updateExistingSalary(existingSalaryOpt.get(), monthlyLessons);
            } else {
                createNewSalary(teacherId, yearMonth, monthlyLessons, currentMonth);
            }
        }
    }

    private Map<YearMonth, List<CalendarDto>> groupLessonsByMonth(List<CalendarDto> lessonsList) {
        return lessonsList.stream()
                .collect(Collectors.groupingBy(lesson -> YearMonth.from(lesson.getStartDate())));
    }

    private Optional<Salary> findExistingSalary(List<Salary> salaryList, YearMonth yearMonth) {
        return salaryList.stream()
                .filter(s -> YearMonth.from(s.getDate()).equals(yearMonth))
                .findFirst();
    }
    
    private void updateExistingSalary(Salary existingSalary, List<CalendarDto> monthlyLessons) {
        YearMonth currentMonth = YearMonth.now();
        YearMonth salaryMonth = YearMonth.from(existingSalary.getDate());

        if (salaryMonth.isBefore(currentMonth)) {
            existingSalary.setStatus(Status.FINISHED);
        }

        Set<String> existingLessonIds = existingSalary.getLessons().stream()
                .map(CalendarDto::getId)
                .collect(Collectors.toSet());

        Set<String> updatedLessonIds = monthlyLessons.stream()
                .map(CalendarDto::getId)
                .collect(Collectors.toSet());

        List<CalendarDto> newLessons = findNewLessons(existingLessonIds, monthlyLessons);
        List<CalendarDto> removedLessons = findRemovedLessons(existingSalary, updatedLessonIds);

        existingSalary.getLessons().removeAll(removedLessons);
        existingSalary.getLessons().addAll(newLessons);

        long newPayoutAmount = calculateTotalPayout(existingSalary.getLessons());
        existingSalary.setPayoutAmount(newPayoutAmount);

        saveSalary(existingSalary);
    }


    private List<CalendarDto> findNewLessons(Set<String> existingLessonIds, List<CalendarDto> monthlyLessons) {
        return monthlyLessons.stream()
                .filter(lesson -> !existingLessonIds.contains(lesson.getId()))
                .toList();
    }

    private List<CalendarDto> findRemovedLessons(Salary existingSalary, Set<String> updatedLessonIds) {
        return existingSalary.getLessons().stream()
                .filter(lesson -> !updatedLessonIds.contains(lesson.getId()))
                .toList();
    }

    private long calculateTotalPayout(List<CalendarDto> lessons) {
        return lessons.stream()
                .mapToLong(CalendarDto::getPrice)
                .sum();
    }

    private void createNewSalary(Long teacherId, YearMonth yearMonth, List<CalendarDto> monthlyLessons, YearMonth currentMonth) {
        Salary newSalary = new Salary();
        newSalary.setId(UUID.randomUUID().toString());
        newSalary.setTeacherId(teacherId);
        newSalary.setDate(yearMonth.atDay(1).atStartOfDay());
        newSalary.setLessons(new ArrayList<>(monthlyLessons));

        newSalary.setPayoutAmount(calculateTotalPayout(monthlyLessons));
        newSalary.setStatus(yearMonth.isBefore(currentMonth) ? Status.FINISHED : Status.IN_PROGRESS);

        saveSalary(newSalary);
    }


    @Override
    public Salary saveSalary(Salary salary) {
        return salaryRepo.save(salary);
    }

    @Override
    public void deleteSalaryById(String id) {
        salaryRepo.deleteById(id);
    }


}
