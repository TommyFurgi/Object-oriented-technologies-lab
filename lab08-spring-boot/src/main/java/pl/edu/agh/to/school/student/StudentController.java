package pl.edu.agh.to.school.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(path = "students")
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<Student> getStudents() {
        return studentService.getStudents();
    }

    @GetMapping("/student")
    public Student getOneStudent(@RequestParam("indexNumber") String indexNumber) {
        return studentService.getStudentByIndexNumber(indexNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{indexNumber}/grades")
    public GradeDto giveGrade(@PathVariable("indexNumber") String indexNumber, @RequestBody GradeDto gradeRequest) {
        return studentService.addGradeToStudent(indexNumber, gradeRequest.courseId, gradeRequest.value)
                .map(grade -> new GradeDto(grade.getGradeValue(), grade.getCourse().getId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{indexNumber}/mean")
    public Float getMeanGrade(@PathVariable("indexNumber") String indexNumber) {
        return studentService.getMeanGrade(indexNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private record GradeDto(int value, int courseId) {}
}
