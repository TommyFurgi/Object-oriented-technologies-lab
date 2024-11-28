package pl.edu.agh.to.school;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;
import pl.edu.agh.to.school.course.Course;
import pl.edu.agh.to.school.course.CourseRepository;
import pl.edu.agh.to.school.grade.Grade;
import pl.edu.agh.to.school.grade.GradeRepository;
import pl.edu.agh.to.school.student.Student;
import pl.edu.agh.to.school.student.StudentRepository;
import pl.edu.agh.to.school.student.StudentService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@ExtendWith(SpringExtension.class)
public class StudentServiceTest {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @BeforeEach
    public void setUp() {
    }

    public void clearRepositories() {
        this.courseRepository.deleteAll();
        this.studentRepository.deleteAll();
        this.gradeRepository.deleteAll();
    }

    @Test
    @Transactional
    public void testGetStudentExistsByIndex() {
        clearRepositories();

        String indexNumber = "111";

        Student student = new Student("Jan", "Kowalski", LocalDate.now(), indexNumber);
        studentRepository.save(student);

        Optional<Student> actualStudent = studentService.getStudentByIndexNumber(indexNumber);

        assertThat(actualStudent).isPresent();
        assertThat(actualStudent.get().getIndexNumber()).isEqualTo(indexNumber);
    }

    @Test
    @Transactional
    public void testGetStudentDoesNotExistByIndex() {
        clearRepositories();

        String indexNumber = "848565";

        Optional<Student> actualStudent = studentService.getStudentByIndexNumber(indexNumber);

        assertThat(actualStudent).isNotPresent();
    }

    @Test
    public void testGetMeanGradeWhenStudentExists() {
        String indexNumber = "123456";
        Float expectedMean = 4.5f;

        Optional<Float> actualMean = studentService.getMeanGrade(indexNumber);

        assertThat(actualMean).isPresent();
        assertThat(actualMean.get()).isEqualTo(expectedMean);
    }

    @Test
    @Transactional
    public void testGiveGradeToStudent() {
        clearRepositories();

        Student student1 = new Student("Henryk", "Menczystaty", LocalDate.now(), "123");
        Student student2 = new Student("Jan", "Kowalski", LocalDate.now(), "111");
        Course course = new Course("Technologie obiektowe");

        courseRepository.save(course);
        course.assignStudent(student1);
        course.assignStudent(student2);

        Grade firstGrade = new Grade(5, course);
        student1.giveGrade(firstGrade);
        gradeRepository.save(firstGrade);
        studentRepository.saveAll(List.of(student1, student2));

        int newGradeValue = 4;

        Optional<Grade> gradeForStudent2 = studentService.addGradeToStudent(student2.getIndexNumber(), course.getId(), newGradeValue);

        assertThat(gradeForStudent2).isPresent();
        assertEquals(newGradeValue, gradeForStudent2.get().getGradeValue());

        Optional<Grade> gradeForStudent1 = studentService.addGradeToStudent(student1.getIndexNumber(), course.getId(), newGradeValue);

        assertThat(gradeForStudent1).isNotPresent();
    }
}
