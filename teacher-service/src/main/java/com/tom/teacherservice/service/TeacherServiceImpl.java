package com.tom.teacherservice.service;

import com.tom.teacherservice.exception.TeacherError;
import com.tom.teacherservice.exception.TeacherException;
import com.tom.teacherservice.model.Status;
import com.tom.teacherservice.model.Teacher;
import com.tom.teacherservice.repo.TeacherRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;

    private static Logger logger = LoggerFactory.getLogger(TeacherServiceImpl.class);

    //sprawdzone
    @Override
    public List<Teacher> getTeachers(Status status) {
        if (status != null) {
            logger.info("Fetching teachers with status: {}", status);
            return teacherRepository.findAllByStatus(status);
        }
        logger.info("Fetching teachers without status.");
        return teacherRepository.findAll();
    }

    @Override
    public List<Teacher> getTeachersByIdNumber(List<Long> idNumbers) {
        logger.info("Fetching teachers list.");
        return teacherRepository.findAllByIdIn(idNumbers);
    }

    @Override
    public Teacher getTeacherById(Long id) {
        logger.info("Fetching teacher by id: {}", id);
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new TeacherException(TeacherError.TEACHER_NOT_FOUND));
        if (!Status.ACTIVE.equals(teacher.getStatus())) {
            logger.info("Teacher is not active");
            throw new TeacherException(TeacherError.TEACHER_IS_NOT_ACTIVE);
        }
        return teacher;
    }

    @Override
    public List<Teacher> getTeachersByIdNumberNotEqual(List<Long> idNumbers) {
        logger.info("Fetching teachers where id number is not equal: {}", idNumbers.toString());
        return teacherRepository.findAllByIdNotInAndStatus(idNumbers, Status.ACTIVE);
    }

    @Override
    public Teacher addTeacher(Teacher teacher) {
        logger.info("Trying create new teacher.");
        validateTeacherEmailExists(teacher.getEmail());
        teacher.setStatus(Status.ACTIVE);
        return teacherRepository.save(teacher);
    }

    @Override
    public void deleteTeacher(Long id) {
        logger.info("Trying deleteTeacher with id: {}.", id);
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new TeacherException(TeacherError.TEACHER_NOT_FOUND));
        teacher.setStatus(Status.INACTIVE);
        teacherRepository.save(teacher);
    }

    @Override
    public Teacher patchTeacher(Long id, Teacher teacher) {
        logger.info("patchTeacher teacherId: {}", id);
        Teacher teacherFromDb = teacherRepository.findById(id)
                .orElseThrow(() -> new TeacherException(TeacherError.TEACHER_NOT_FOUND));
        if (teacher.getFirstName() != null) {
            logger.info("Changing first name");
            teacherFromDb.setFirstName(teacher.getFirstName());
        }
        if (teacher.getLastName() != null) {
            logger.info("Changing last name");
            teacherFromDb.setLastName(teacher.getLastName());
        }
        if (teacher.getStatus() != null) {
            logger.info("Changing status");
            teacherFromDb.setStatus(teacher.getStatus());
        }
        return teacherRepository.save(teacherFromDb);
    }
    //nie sprawdzone

    @Override
    public Teacher getTeacherByEmail(String email) {
        Teacher teacher = teacherRepository.findByEmail(email)
                .orElseThrow(() -> new TeacherException(TeacherError.TEACHER_NOT_FOUND));
        if (!Status.ACTIVE.equals(teacher.getStatus())) {
            throw new TeacherException(TeacherError.TEACHER_IS_NOT_ACTIVE);
        }
        return teacher;
    }

    private void validateTeacherEmailExists(String email) {
        if (teacherRepository.existsByEmail(email)) {
            throw new TeacherException(TeacherError.TEACHER_EMAIL_ALREADY_EXISTS);
        }
    }

    @Override
    public Teacher putTeacher(Long id, Teacher teacher) {
        return teacherRepository.findById(id)
                .map(teacherFromDb -> {
                    if (!teacherFromDb.getEmail().equals(teacher.getEmail())
                            && teacherRepository.existsByEmail(teacher.getEmail())) {
                        throw new TeacherException(TeacherError.TEACHER_EMAIL_ALREADY_EXISTS);
                    }
                    teacherFromDb.setFirstName(teacher.getFirstName());
                    teacherFromDb.setLastName(teacher.getLastName());
                    teacherFromDb.setStatus(teacher.getStatus());
                    return teacherRepository.save(teacherFromDb);
                }).orElseThrow(() -> new TeacherException(TeacherError.TEACHER_NOT_FOUND));
    }
}
