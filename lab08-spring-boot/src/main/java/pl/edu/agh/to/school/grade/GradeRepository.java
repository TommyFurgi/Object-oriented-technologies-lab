package pl.edu.agh.to.school.grade;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Integer> {
    @Query(value = "SELECT g.* FROM STUDENT s " +
            "JOIN STUDENT_GRADES sg ON sg.STUDENT_ID = s.ID " +
            "JOIN GRADE g ON g.ID = sg.GRADES_ID " +
            "WHERE s.INDEX_NUMBER = :indexNumber AND g.COURSE_ID = :courseId",
            nativeQuery = true)
    Optional<Grade> findGradeByStudentAndCourse(@Param("indexNumber") String indexNumber, @Param("courseId") int courseId);
}
