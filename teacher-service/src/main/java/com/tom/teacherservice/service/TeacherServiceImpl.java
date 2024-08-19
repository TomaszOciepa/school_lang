package com.tom.teacherservice.service;

import com.tom.teacherservice.exception.TeacherError;
import com.tom.teacherservice.exception.TeacherException;
import com.tom.teacherservice.model.Status;
import com.tom.teacherservice.model.Teacher;
import com.tom.teacherservice.repo.TeacherRepository;
import com.tom.teacherservice.security.AuthenticationContext;
import com.tom.teacherservice.security.JwtUtils;
import feign.FeignException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final AuthenticationContext authenticationContext;
    private final JwtUtils jwtUtils;
    private final CalendarServiceClient calendarServiceClient;
    private final CourseServiceClient courseServiceClient;
    private final KeycloakServiceClient keycloakServiceClient;
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

        Authentication authentication = authenticationContext.getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_admin"));


        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new TeacherException(TeacherError.TEACHER_NOT_FOUND));
        if (!Status.ACTIVE.equals(teacher.getStatus()) && !isAdmin) {
            logger.info("Teacher is not active");
            throw new TeacherException(TeacherError.TEACHER_IS_NOT_ACTIVE);
        }
        return teacher;
    }

    @Override
    public void teacherIsActive(Long id) {
        logger.info("teacherIsActive() teacherId: {}", id);

        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new TeacherException(TeacherError.TEACHER_NOT_FOUND));
        if (!Status.ACTIVE.equals(teacher.getStatus())) {
            logger.info("Teacher is not active");
            throw new TeacherException(TeacherError.TEACHER_IS_NOT_ACTIVE);
        }
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
        teacher.setStatus(Status.INACTIVE);
        return teacherRepository.save(teacher);
    }

    @Override
    public void deactivateTeacherById(Long id) {
        logger.info("Trying deleteTeacher with id: {}.", id);
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new TeacherException(TeacherError.TEACHER_NOT_FOUND));

        try{
            keycloakServiceClient.enabledAccount(teacher.getEmail(), false);
        }catch (FeignException ex) {
            logger.error("FeignException occurred: {}", ex.getMessage());
        }

        teacher.setStatus(Status.INACTIVE);
        teacherRepository.save(teacher);
    }

    @Override
    public void deleteTeacherById(Long id) {
        logger.info("Trying deleteTeacher with id: {}.", id);
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new TeacherException(TeacherError.TEACHER_NOT_FOUND));
        calendarServiceClient.deleteLessonsByTeacherId(id);
        try {
            courseServiceClient.removeTeacherWithAllCourses(id);
        } catch (FeignException ex) {
            logger.error("FeignException occurred: {}", ex.getMessage());
        }
        teacherRepository.deleteById(id);
        keycloakServiceClient.deleteAccount(teacher.getEmail());
    }

    @Override
    public ResponseEntity<Void> patchTeacher(Long id, Teacher teacher) {
        logger.info("patchTeacher teacherId: {}", id);
        Teacher teacherFromDb = teacherRepository.findById(id)
                .orElseThrow(() -> new TeacherException(TeacherError.TEACHER_NOT_FOUND));

        boolean isTeacher = authenticationContext();

        if(isTeacher){
            String emailFromJwt = jwtUtils.getUserEmailFromJwt();
            if(isTeacher && !emailFromJwt.equals(teacherFromDb.getEmail())){
                throw new TeacherException(TeacherError.TEACHER_OPERATION_FORBIDDEN);
            }
        }
        
        try {
            keycloakServiceClient.updateAccount(teacher, teacherFromDb.getEmail());
        } catch (FeignException ex) {
            logger.error("FeignException occurred: {}", ex.getMessage());
            if(ex.status() == 409){
                throw new TeacherException(TeacherError.TEACHER_EMAIL_ALREADY_EXISTS);
            }
        }

        boolean dbUpdateNeeded = false;

        if (teacher.getFirstName() != null && !teacher.getFirstName().equals(teacherFromDb.getFirstName())) {
            logger.info("Changing first name");
            teacherFromDb.setFirstName(teacher.getFirstName());
            dbUpdateNeeded = true;
        }
        if (teacher.getLastName() != null && !teacher.getLastName().equals(teacherFromDb.getLastName())) {
            logger.info("Changing last name");
            teacherFromDb.setLastName(teacher.getLastName());
            dbUpdateNeeded = true;
        }
        if (teacher.getStatus() != null && !teacher.getStatus().equals(teacherFromDb.getStatus())) {
            logger.info("Changing status");
            teacherFromDb.setStatus(teacher.getStatus());
            dbUpdateNeeded = true;
        }
        if (teacher.getEmail() != null && !teacher.getEmail().equals(teacherFromDb.getEmail())) {
            logger.info("Changing email");
            teacherFromDb.setEmail(teacher.getEmail());
            dbUpdateNeeded = true;
        }

        if (dbUpdateNeeded) {
            teacherRepository.save(teacherFromDb);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @Override
    public Teacher getTeacherByEmail(String email) {
        logger.info("Fetching teacher by email: {}", email);
        Teacher teacher = teacherRepository.findByEmail(email)
                .orElseThrow(() -> new TeacherException(TeacherError.TEACHER_NOT_FOUND));
        if (!Status.ACTIVE.equals(teacher.getStatus())) {
            logger.info("Teacher is not active.");
            throw new TeacherException(TeacherError.TEACHER_IS_NOT_ACTIVE);
        }
        return teacher;
    }

    @Override
    public void restoreTeacherAccount(Long id) {
        Teacher teacher = getTeacherById(id);

        try{
            keycloakServiceClient.enabledAccount(teacher.getEmail(), true);
        }catch (FeignException ex) {
            logger.error("FeignException occurred: {}", ex.getMessage());
        }
        teacher.setStatus(Status.ACTIVE);
        teacherRepository.save(teacher);
    }
    //nie sprawdzone

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

    private boolean authenticationContext() {
        boolean result = false;
        Authentication authentication = authenticationContext.getAuthentication();
        boolean isTeacher = authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_teacher"));
        if (isTeacher) {
            result = true;
        }
        return result;
    }
}
