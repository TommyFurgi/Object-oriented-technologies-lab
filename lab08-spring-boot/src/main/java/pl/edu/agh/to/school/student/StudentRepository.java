package pl.edu.agh.to.school.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    Optional<Student> findByIndexNumber(String indexNumber);


    @Query(value = "SELECT AVG(g.GRADE_VALUE) " +
            "FROM STUDENT_GRADES sg " +
            "JOIN STUDENT s ON s.ID = sg.STUDENT_ID " +
            "JOIN GRADE g ON g.ID = sg.GRADES_ID " +
            "WHERE s.INDEX_NUMBER = :studentIndexNumber", nativeQuery = true)
    Optional<Float> getMeanGrade(@Param("studentIndexNumber") String indexNumber);
}
