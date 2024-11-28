package pl.edu.agh.to.school.course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

    @Query(value = "SELECT AVG(g.GRADE_VALUE) " +
            "FROM COURSE c " +
            "JOIN COURSE_STUDENTS cs ON cs.COURSE_ID = c.ID " +
            "JOIN STUDENT s ON s.ID = cs.STUDENTS_ID " +
            "JOIN STUDENT_GRADES sg ON sg.STUDENT_ID = s.ID " +
            "JOIN GRADE g ON g.ID = sg.GRADES_ID " +
            "WHERE g.COURSE_ID = :courseId",
            nativeQuery = true)
    Optional<Float> calculateMeanForCourse(@Param("courseId") int id);
}
