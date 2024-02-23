package studentadmin.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import studentadmin.DTO.AddStudentsToCourseDTO;
import studentadmin.DTO.UpdateCourseTeacherDTO;
import studentadmin.models.Course;
import studentadmin.models.Student;
import studentadmin.models.Teacher;
import studentadmin.services.CourseService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public List<Course> getAllCourses(){
        List<Course> course = courseService.findAll();
        return course;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourse(@PathVariable int id){
        Optional<Course> course = courseService.findById(id);
        return ResponseEntity.of(course);
    }

    @GetMapping("/{id}/teacher")
    public ResponseEntity<Teacher> getCourseTeacher(@PathVariable int id){
        return courseService.getCourseTeacher(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    
    @GetMapping("/{id}/students")
    public ResponseEntity<List<Student>> getCourseStudents(@PathVariable int id){
        return courseService.getCourseStudents(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Course> createCourse(@RequestBody Course course){
        return courseService.createCourse(course);
    }

    @PostMapping("/{id}/students")
    public ResponseEntity<?> addStudentsToCourse(@PathVariable int id, @RequestBody AddStudentsToCourseDTO studentsDTO){
        return courseService.addStudentsToCourse(id, studentsDTO);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable int id, @RequestBody Course course){
        Optional<Course> original = courseService.updateCourse(id, course);
        return original.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
      
    }

    @PatchMapping("/{id}/teacher")
    public ResponseEntity<Course> updateCourseTeacher(@PathVariable int id, @RequestBody UpdateCourseTeacherDTO teacherDTO){
        Optional<Course> original = courseService.updateCourseTeacher(id, teacherDTO);
        return original.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
      
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Course> deleteCourse(@PathVariable int id){
        return ResponseEntity.of(courseService.deleteCourse(id));
    }

   @DeleteMapping("/{id}/students/{studentId}")
    public ResponseEntity<?> removeStudentFromCouse(@PathVariable int id, @PathVariable int studentId){
       return courseService.removeStudentFromCourse(id, studentId);
   }
    
}
