package com.tom.studentservice.service;

import com.tom.studentservice.exception.StudentError;
import com.tom.studentservice.exception.StudentException;
import com.tom.studentservice.model.Status;
import com.tom.studentservice.model.Student;
import com.tom.studentservice.repo.StudentRepository;
import com.tom.studentservice.security.AuthenticationContext;
import com.tom.studentservice.security.JwtUtils;
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
public class StudentServiceImpl implements StudentService {

    private static Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);
    private final StudentRepository studentRepository;
    private final AuthenticationContext authenticationContext;
    private final CalendarServiceClient calendarServiceClient;
    private final CourseServiceClient courseServiceClient;
    private final JwtUtils jwtUtils;
    private final KeycloakServiceClient keycloakServiceClient;

    //sprawdzone
    @Override
    public List<Student> getStudentsByIdNumbers(List<Long> idNumbers) {
        logger.info("Fetching students by id list numbers.");
        if (idNumbers.isEmpty()) {
            logger.warn("Id list numbers is empty.");
        }
        return studentRepository.findAllByIdIn(idNumbers);
    }

    @Override
    public List<Student> getStudents(Status status) {
        logger.info("Fetching all students.");
        if (status != null) {
            logger.info("Fetching all students with status: {}", status);
            return studentRepository.findAllByStatus(status);
        }
        logger.info("Fetching all students without status.");
        return studentRepository.findAll();
    }

    @Override
    public List<Student> getStudentsByIdNumberNotEqual(List<Long> idNumbers) {
        logger.info("Fetching students where id numbers not equal: idNumbers: {}", idNumbers);
        return studentRepository.findAllByIdNotInAndStatus(idNumbers, Status.ACTIVE);
    }

    @Override
    public Student getStudentById(Long id) {
        logger.info("Fetching student by id: {}", id);
        boolean isNotStudent = authenticationContext();
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentException(StudentError.STUDENT_NOT_FOUND));
        if (!Status.ACTIVE.equals(student.getStatus()) && !isNotStudent) {
            logger.info("Student is not Active.");
            throw new StudentException(StudentError.STUDENT_IS_NOT_ACTIVE);
        }

        String emailFromJwt = jwtUtils.getUserEmailFromJwt();
        if(!isNotStudent && !emailFromJwt.equals(student.getEmail())){
            throw new StudentException(StudentError.STUDENT_OPERATION_FORBIDDEN);
        }

        return student;
    }


    private boolean authenticationContext() {
        boolean result = false;
        Authentication authentication = authenticationContext.getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_admin"));
        boolean isTeacher = authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_teacher"));
        if (isAdmin || isTeacher) {
            result = true;
        }
        return result;
    }

    @Override
    public Student addStudent(Student student) {
        logger.info("Trying create new student.");
//        String emailFromJwt = jwtUtils.getUserEmailFromJwt();
//        boolean isNotStudent = authenticationContext();
//        if(!isNotStudent && !emailFromJwt.equals(student.getEmail())){
//            throw new StudentException(StudentError.STUDENT_OPERATION_FORBIDDEN);
//        }
        validateStudentEmailExists(student.getEmail());
        student.setStatus(Status.ACTIVE);
        return studentRepository.save(student);
    }

    @Override
    public ResponseEntity<Void> patchStudent(Long id, Student student) {
        logger.info("patchStudent studentId: {}", id);
        Student studentFromDb = studentRepository.findById(id)
                .orElseThrow(() -> new StudentException(StudentError.STUDENT_NOT_FOUND));

        boolean isNotStudent = authenticationContext();

        if(!isNotStudent){
            String emailFromJwt = jwtUtils.getUserEmailFromJwt();
            if(!isNotStudent && !emailFromJwt.equals(studentFromDb.getEmail())){
                throw new StudentException(StudentError.STUDENT_OPERATION_FORBIDDEN);
            }
        }

        try {
            keycloakServiceClient.updateAccount(student, studentFromDb.getEmail());
        } catch (FeignException ex) {
            logger.error("FeignException occurred: {}", ex.getMessage());
            if(ex.status() == 409){
                throw new StudentException(StudentError.STUDENT_EMAIL_ALREADY_EXISTS);
            }
        }

        boolean dbUpdateNeeded = false;

        if (student.getFirstName() != null && !student.getFirstName().equals(studentFromDb.getFirstName())) {
            logger.info("Changing first name");
            studentFromDb.setFirstName(student.getFirstName());
            dbUpdateNeeded = true;
        }
        if (student.getLastName() != null && !student.getLastName().equals(studentFromDb.getLastName())) {
            logger.info("Changing last name");
            studentFromDb.setLastName(student.getLastName());
            dbUpdateNeeded = true;
        }
        if (student.getStatus() != null && !student.getStatus().equals(studentFromDb.getStatus())) {
            logger.info("Changing status");
            studentFromDb.setStatus(student.getStatus());
            dbUpdateNeeded = true;
        }

        if(student.getEmail() != null && !student.getEmail().equals(studentFromDb.getEmail())){
            logger.info("Changing email");
            studentFromDb.setEmail(student.getEmail());
            dbUpdateNeeded = true;
        }

        if(dbUpdateNeeded){
            studentRepository.save(studentFromDb);
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.noContent().build();
        }
    }

    @Override
    public void deactivateStudentById(Long id) {
        logger.info("deleteStudent studentId: {}", id);
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentException(StudentError.STUDENT_NOT_FOUND));
        try{
            keycloakServiceClient.enabledAccount(student.getEmail(), false);
        }catch (FeignException ex) {
            logger.error("FeignException occurred: {}", ex.getMessage());
        }

        try {
            calendarServiceClient.deactivateStudent(id);
        } catch (FeignException ex) {
            logger.error("FeignException occurred: {}", ex.getMessage());
        }

        try {
            courseServiceClient.deactivateStudent(id);
        } catch (FeignException ex) {
            logger.error("FeignException occurred: {}", ex.getMessage());
        }

        student.setStatus(Status.INACTIVE);
        studentRepository.save(student);
    }

    @Override
    public void restoreStudentAccount(Long id) {
        Student student = getStudentById(id);

        try{
            keycloakServiceClient.enabledAccount(student.getEmail(), true);
        }catch (FeignException ex) {
            logger.error("FeignException occurred: {}", ex.getMessage());
        }

        student.setStatus(Status.ACTIVE);
        studentRepository.save(student);
    }

    @Override
    public void deleteStudentById(Long id) {
        logger.info("Trying deleteStudent with id: {}.", id);
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentException(StudentError.STUDENT_NOT_FOUND));

        try {
            calendarServiceClient.deleteStudentWithAllLessons(id);
        } catch (FeignException ex) {
            logger.error("FeignException occurred: {}", ex.getMessage());
        }

        try {
            courseServiceClient.removeStudentWithAllCourses(id);
        } catch (FeignException ex) {
            logger.error("FeignException occurred: {}", ex.getMessage());
        }
        studentRepository.deleteById(id);
        keycloakServiceClient.deleteAccount(student.getEmail());
    }

    private void validateStudentEmailExists(String email) {
        logger.info("validateStudentEmailExists: {}", email);
        if (studentRepository.existsByEmail(email)) {
            logger.warn("Student email already exists.");
            throw new StudentException(StudentError.STUDENT_EMAIL_ALREADY_EXISTS);
        }
    }

    @Override
    public void studentIsActive(Long id) {
        logger.info("studentIsActive() studentId: {}", id);

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentException(StudentError.STUDENT_NOT_FOUND));
        if (!Status.ACTIVE.equals(student.getStatus())) {
            logger.info("Student is not active");
            throw new StudentException(StudentError.STUDENT_IS_NOT_ACTIVE);
        }
    }

    @Override
    public Student getStudentByEmail(String email) {
        logger.info("Fetching student by email: {}", email);
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new StudentException(StudentError.STUDENT_NOT_FOUND));
        if (!Status.ACTIVE.equals(student.getStatus())) {
            logger.info("Students is not active.");
            throw new StudentException(StudentError.STUDENT_IS_NOT_ACTIVE);
        }
        return student;
    }

    //nie sprawdzone


    @Override
    public List<Student> getStudentsByEmails(List<String> emails) {
        return studentRepository.findAllByEmailIn(emails);
    }

}
