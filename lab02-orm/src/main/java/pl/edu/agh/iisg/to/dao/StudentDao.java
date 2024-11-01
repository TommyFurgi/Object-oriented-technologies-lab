package pl.edu.agh.iisg.to.dao;

import jakarta.persistence.PersistenceException;
import org.hibernate.Session;
import pl.edu.agh.iisg.to.model.Student;
import pl.edu.agh.iisg.to.session.SessionService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class StudentDao extends GenericDao<Student> {

    public StudentDao(SessionService sessionService) {
        super(sessionService, Student.class);
    }

    public Optional<Student> create(final String firstName, final String lastName, final int indexNumber) {
        // TODO - implement
        return save(new Student(firstName, lastName, indexNumber));
    }

    public List<Student> findAll() {
        // TODO - implement
        try {
            Session session = currentSession();
            List<Student> studentList = session.createQuery("SELECT s FROM Student s ORDER BY s.lastName", Student.class)
                    .getResultList();

            return studentList;
        } catch (PersistenceException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    public Optional<Student> findByIndexNumber(final int indexNumber) {
        // TODO - implement
        try {
            Session session = currentSession();
            Optional<Student> student = session.createQuery("SELECT s FROM Student s WHERE s.indexNumber = :indexNumber", Student.class)
                    .setParameter("indexNumber",  indexNumber)
                    .uniqueResultOptional();

            return student;
        } catch (PersistenceException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }
}
