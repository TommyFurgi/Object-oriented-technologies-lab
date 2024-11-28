package pl.edu.agh.to.school.student;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.to.school.course.Course;
import pl.edu.agh.to.school.course.CourseService;
import pl.edu.agh.to.school.grade.Grade;
import pl.edu.agh.to.school.grade.GradeService;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final CourseService courseService;
    private final GradeService gradeService;

    @Autowired
    public StudentService(StudentRepository studentRepository, CourseService courseService, GradeService gradeService) {
        this.studentRepository = studentRepository;
        this.courseService = courseService;
        this.gradeService = gradeService;
    }

    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentByIndexNumber(String indexNumber) {
        return studentRepository.findByIndexNumber(indexNumber);
    }

    @Transactional
    public Optional<Grade> addGradeToStudent(String indexNumber, int courseId, int gradeValue) {
        Optional<Student> student = studentRepository.findByIndexNumber(indexNumber);
        if (student.isEmpty()) {
            return Optional.empty();
        }

        Optional<Course> course = courseService.getCourseById(courseId);
        if (course.isEmpty()) {
            return Optional.empty();
        }

        Optional<Grade> grade = gradeService.addGrade(gradeValue, course.get(), indexNumber);
        if (grade.isEmpty()) {
            return Optional.empty();
        }

        student.get().giveGrade(grade.get());
        studentRepository.save(student.get());

        return grade;
    }

    public Optional<Float> getMeanGrade(String indexNumber) {
        return this.studentRepository.getMeanGrade(indexNumber);
    }
}
