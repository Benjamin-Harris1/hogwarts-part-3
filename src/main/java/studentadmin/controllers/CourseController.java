package studentadmin.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import studentadmin.models.Course;
import studentadmin.models.Teacher;
import studentadmin.repositories.CourseRepository;
import studentadmin.repositories.StudentRepository;
import studentadmin.repositories.TeacherRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/courses")
public class CourseController {
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    public CourseController(CourseRepository courseRepository, StudentRepository studentRepository, TeacherRepository teacherRepository) {
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }

    @GetMapping
    public List<Course> getAllCourses(){
        List<Course> course = courseRepository.findAll();
        return course;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourse(@PathVariable int id){
        Optional<Course> course = courseRepository.findById(id);
        return ResponseEntity.of(course);
    }

    @GetMapping("/{id}/teacher")
    public ResponseEntity<Teacher> getCourseTeacher(@PathVariable int id){
        Optional<Course> course = courseRepository.findById(id);
        if (course.isPresent()){
            Teacher teacher = course.get().getTeacher();
            return teacher != null ? ResponseEntity.ok(teacher) : ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.notFound().build();
        }

    }
}
