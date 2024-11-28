package pl.edu.agh.to.school.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pl.edu.agh.to.school.student.Student;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/courses")
public class CourseController {
    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public List<Course> getCourses() {
        return courseService.getCourses();
    }

    @GetMapping("/{id}")
    public List<Student> getStudentsOnCourse(@PathVariable("id") int id) {
        return courseService.getCourseById(id)
                .map(course -> new ArrayList<>(course.getStudents()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}/mean")
    public Float getMeanForCourse(@PathVariable("id") int id) {
        return courseService.getMeanForCourse(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
