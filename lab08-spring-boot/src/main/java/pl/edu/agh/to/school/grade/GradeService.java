package pl.edu.agh.to.school.grade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.to.school.course.Course;

import java.util.Optional;

@Service
public class GradeService {

    private final GradeRepository gradeRepository;

    @Autowired
    public GradeService(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    public Optional<Grade> addGrade(int gradeValue, Course course, String indexNumber) {
        boolean hasGrade = gradeRepository.findGradeByStudentAndCourse(indexNumber, course.getId()).isPresent();
        if (hasGrade) {
            return Optional.empty();
        }

        Grade grade = new Grade(gradeValue, course);
        this.gradeRepository.save(grade);

        return Optional.of(grade);
    }
}