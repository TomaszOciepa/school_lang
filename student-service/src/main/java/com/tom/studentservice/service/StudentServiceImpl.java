package com.tom.studentservice.service;

import com.tom.studentservice.exception.StudentError;
import com.tom.studentservice.exception.StudentException;
import com.tom.studentservice.model.Status;
import com.tom.studentservice.model.Student;
import com.tom.studentservice.repo.StudentRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StudentServiceImpl implements StudentService {

    private static Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);
    private final StudentRepository studentRepository;

    //sprawdzone
    @Override
    public List<Student> getStudentsByIdNumbers(List<Long> idNumbers) {
        logger.info("Fetching students by id list numbers.");
        if(idNumbers.isEmpty()){
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
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentException(StudentError.STUDENT_NOT_FOUND));
        if (!Status.ACTIVE.equals(student.getStatus())) {
            logger.info("Student is not Active.");
            throw new StudentException(StudentError.STUDENT_IS_NOT_ACTIVE);
        }
        return student;
    }

    @Override
    public Student addStudent(Student student) {
        logger.info("Trying create new student.");
        validateStudentEmailExists(student.getEmail());
        student.setStatus(Status.ACTIVE);
        return studentRepository.save(student);
    }

    @Override
    public Student patchStudent(Long id, Student student) {
        logger.info("patchStudent studentId: {}", id);
        Student studentFromDb = studentRepository.findById(id)
                .orElseThrow(() -> new StudentException(StudentError.STUDENT_NOT_FOUND));

        if (student.getFirstName() != null) {
            logger.info("Changing first name");
            studentFromDb.setFirstName(student.getFirstName());
        }
        if (student.getLastName() != null) {
            logger.info("Changing last name");
            studentFromDb.setLastName(student.getLastName());
        }
        if (student.getStatus() != null) {
            logger.info("Changing status");
            studentFromDb.setStatus(student.getStatus());
        }
        return studentRepository.save(studentFromDb);
    }

    @Override
    public void deleteStudent(Long id) {
        logger.info("deleteStudent studentId: {}", id);
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentException(StudentError.STUDENT_NOT_FOUND));
        student.setStatus(Status.INACTIVE);
        studentRepository.save(student);
    }

    private void validateStudentEmailExists(String email) {
        logger.info("validateStudentEmailExists: {}", email);
        if (studentRepository.existsByEmail(email)) {
            logger.warn("Student email already exists.");
            throw new StudentException(StudentError.STUDENT_EMAIL_ALREADY_EXISTS);
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
