package pl.edu.agh.iisg.to.repository;

import pl.edu.agh.iisg.to.dao.StudentDao;
import pl.edu.agh.iisg.to.model.Course;
import pl.edu.agh.iisg.to.model.Student;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StudentRepository implements Repository {
    private final StudentDao studentDao;

    public StudentRepository(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    @Override
    public Optional<Student> add(Object obj) {
        if (obj instanceof Student student) {
            studentDao.save(student);
            return Optional.of(student);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Student> getById(int id) {
        return studentDao.findById(id);
    }

    @Override
    public List<Student> findAll() {
        return studentDao.findAll();
    }

    @Override
    public void remove(Object obj) {
        if (obj instanceof Student student) {
            for (Course course : student.courseSet()) {
                course.studentSet().remove(student);
            }

            student.courseSet().clear();
            studentDao.remove(student);
        }
    }

    public List<Student> findAllByCourseName(String courseName) {
        List<Student> studentList = studentDao.findAll();
        return studentList.stream()
                .filter(student -> student.courseSet().stream()
                        .anyMatch(course -> course.name().equals(courseName)))
                .collect(Collectors.toList());
    }

    public Optional<Student> findByIndexNumber(int indexNumber) {
        return studentDao.findByIndexNumber(indexNumber);
    }
}
