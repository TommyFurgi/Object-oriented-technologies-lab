package pl.edu.agh.iisg.to.service;

import org.hibernate.Transaction;
import org.hibernate.resource.beans.internal.FallbackBeanInstanceProducer;
import pl.edu.agh.iisg.to.dao.CourseDao;
import pl.edu.agh.iisg.to.dao.GradeDao;
import pl.edu.agh.iisg.to.dao.StudentDao;
import pl.edu.agh.iisg.to.model.Course;
import pl.edu.agh.iisg.to.model.Grade;
import pl.edu.agh.iisg.to.model.Student;
import pl.edu.agh.iisg.to.repository.Repository;
import pl.edu.agh.iisg.to.repository.StudentRepository;
import pl.edu.agh.iisg.to.session.TransactionService;

import java.util.*;
import java.util.stream.Collectors;

public class SchoolService {

    private final TransactionService transactionService;

    private final CourseDao courseDao;

    private final GradeDao gradeDao;

    private final StudentRepository studentRepository;

    public SchoolService(TransactionService transactionService, StudentRepository studentRepository, CourseDao courseDao, GradeDao gradeDao) {
        this.transactionService = transactionService;
        this.courseDao = courseDao;
        this.gradeDao = gradeDao;
        this.studentRepository = studentRepository;
    }

    public boolean enrollStudent(final Course course, final Student student) {
        // TODO - implement
        return transactionService.doAsTransaction(() -> {
            if (course.studentSet().contains(student)) {
                return false;
            }
            course.studentSet().add(student);
            student.courseSet().add(course);
            return true;
        }).orElse(false);
    }

    public boolean removeStudent(int indexNumber) {
        // TODO - implement
        return transactionService.doAsTransaction(() -> {
            Optional<Student> studentOpt = studentRepository.findByIndexNumber(indexNumber);
            if (studentOpt.isEmpty())
                return false;

            Student student = studentOpt.get();
//            for (Course course : student.courseSet()) {
//                course.studentSet().remove(student);
//            }

//            student.courseSet().clear();
//            studentDao.remove(student);
            studentRepository.remove(student);
            return true;
        }).orElse(false);
    }

    public boolean gradeStudent(final Student student, final Course course, final float gradeValue) {
        // TODO - implement
        return transactionService.doAsTransaction(() -> {
            Grade grade = new Grade(student, course, gradeValue);
            gradeDao.save(grade);

            student.gradeSet().add(grade);
            course.gradeSet().add(grade);

            return true;
        }).orElse(false);
    }

    public Map<String, List<Float>> getStudentGrades(String courseName) {
        // TODO - implement
        return transactionService.doAsTransaction(() -> {
            Optional<Course> courseOpt = courseDao.findByName(courseName);
//            if (courseOpt.isEmpty())
//                return Collections.emptyMap();
            Course course = courseOpt.get();

            Set<Student> students = course.studentSet();
            Map<String, List<Float>> studentGrades = students.stream()
                    .collect(Collectors.toMap(
                        student -> student.fullName(),
                        student -> student.gradeSet().stream()
                                .filter(grade -> grade.course().equals(course))
                                .map(grade -> grade.grade())
                                .collect(Collectors.toList())
            ));

            return studentGrades;
        }).orElse(Collections.emptyMap());
    }
}
