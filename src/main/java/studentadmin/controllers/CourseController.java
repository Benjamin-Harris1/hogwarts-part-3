package studentadmin.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import studentadmin.models.Course;
import studentadmin.models.Student;
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

    
    @GetMapping("/{id}/students")
    public ResponseEntity<List<Student>> getCourseStudents(@PathVariable int id){
        Optional<Course> original = courseRepository.findById(id);
        if (original.isPresent()){
            Course course = original.get();
            List<Student> students = course.getStudents();
            return ResponseEntity.ok(students);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Course createCourse(@RequestBody Course course){
        return courseRepository.save(course);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable int id, @RequestBody Course course){
        Optional<Course> original = courseRepository.findById(id);
        if (original.isPresent()){
            Course originalCourse = original.get();
            //Opdater course
            originalCourse.setSubject(course.getSubject());
            originalCourse.setSchoolYear(course.getSchoolYear());
            originalCourse.setCurrent(course.isCurrent());
            originalCourse.setTeacher(course.getTeacher());
            originalCourse.setStudents(course.getStudents());

            // Gem og returner opdaterede course
            Course updatedCourse = courseRepository.save(originalCourse);
            return ResponseEntity.ok().body(updatedCourse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/teacher")
    public ResponseEntity<Course> updateCourseTeacher(@PathVariable int id, @RequestBody Teacher teacher){
        Optional<Course> original = courseRepository.findById(id);
        if (original.isPresent()){
            Course course = original.get();
            course.setTeacher(teacher);
            courseRepository.save(course);
            return ResponseEntity.ok(course);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/students/{studentId}")
    public ResponseEntity<Course> addStudentToCourse(@PathVariable int id, @PathVariable int studentId){
        Optional<Course> original = courseRepository.findById(id);
        Optional<Student> originalStudent = studentRepository.findById(studentId);
        if (original.isPresent() && originalStudent.isPresent()) {
            Course course = original.get();
            Student student = originalStudent.get();
            course.getStudents().add(student);
            courseRepository.save(course);
            return ResponseEntity.ok(course);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Course> deleteCourse(@PathVariable int id){
        Optional<Course> course = courseRepository.findById(id);
        courseRepository.deleteById(id);
        return ResponseEntity.of(course);
    }

   @DeleteMapping("/{id}/teacher")
   public ResponseEntity<Course> removeTeacherFromCourse(@PathVariable int id){
        Optional<Course> course = courseRepository.findById(id);
        if (course.isPresent()) {
            Course updatedCourse = course.get();
            updatedCourse.setTeacher(null);
            courseRepository.save(updatedCourse);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
   }

   @DeleteMapping("/{id}/students/{studentId}")
    public ResponseEntity<Course> removeStudentFromCouse(@PathVariable int id, @PathVariable int studentId){
        Optional<Course> original = courseRepository.findById(id);
        Optional<Student> originalStudent = studentRepository.findById(studentId);
        if (original.isPresent() && originalStudent.isPresent()) {
            Course course = original.get();
            Student student = originalStudent.get();
            if (course.getStudents().remove(student)) {
                courseRepository.save(course);
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.notFound().build();
   }
    
}
