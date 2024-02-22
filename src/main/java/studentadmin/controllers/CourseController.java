package studentadmin.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import studentadmin.DTO.AddStudentsToCourseDTO;
import studentadmin.DTO.UpdateCourseTeacherDTO;
import studentadmin.models.Course;
import studentadmin.models.Student;
import studentadmin.models.Teacher;
import studentadmin.repositories.CourseRepository;
import studentadmin.repositories.StudentRepository;
import studentadmin.repositories.TeacherRepository;
import studentadmin.services.StudentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/courses")
public class CourseController {
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final StudentService studentService;

    public CourseController(CourseRepository courseRepository, StudentRepository studentRepository, TeacherRepository teacherRepository, StudentService studentService) {
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.studentService = studentService;
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

    @PostMapping("/{id}/students")
    public ResponseEntity<?> addStudentsToCourse(@PathVariable int id, @RequestBody AddStudentsToCourseDTO studentsDTO){
        Optional<Course> original = courseRepository.findById(id);
        if (!original.isPresent()){
            return ResponseEntity.notFound().build();
        }
        // Henter course
        Course course = original.get();
        // Opretter nyt array som fundne students bliver tilføjet til
        List<Student> studentsToAdd = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();

        // Looper over students og matcher enten ID eller name til en tilvsarende student i DB, og tilføjer til studentsToAdd array
        for (AddStudentsToCourseDTO.StudentIdentifier studentIdentifier : studentsDTO.getStudents()) {
            if (studentIdentifier.getId() > 0) {
                studentRepository.findById(studentIdentifier.getId()).ifPresent(student -> {
                    if (student.getSchoolYear() == course.getSchoolYear()){
                        // Tjek for duplikater
                        boolean isAlreadyAdded = course.getStudents().contains(student);
                        if (!isAlreadyAdded){
                            studentsToAdd.add(student);
                        } else {
                            errorMessages.add("Student with ID " + student.getId() + " is already on the course!");
                        }
                    } else {
                        errorMessages.add("Student with ID " + student.getId() + " does not have the same school year as the course");
                    }
                });
            } else if (studentIdentifier.getName() != null) {
                List<Student> foundStudents = studentService.findStudentsByName(studentIdentifier.getName());
                for (Student student : foundStudents) {
                    if (student.getSchoolYear() == course.getSchoolYear()) {
                        boolean isAlreadyAdded = course.getStudents().contains(student);
                        if (!isAlreadyAdded){
                            studentsToAdd.add(student);
                        } else {
                            errorMessages.add("Student with name " + student.getFirstName() + " " + student.getLastName() + " is already on the course!");
                        }
                    } else {
                        errorMessages.add("Student with name " + student.getFirstName() + " " + student.getLastName() + " does not have the same school year as the course");
                    }
                }
            }
        }
        if (!studentsToAdd.isEmpty()) {
            course.getStudents().addAll(studentsToAdd);
            courseRepository.save(course);
        } 
        if (!errorMessages.isEmpty()){
            return ResponseEntity.badRequest().body(errorMessages);
        }
        return ResponseEntity.ok(course);
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

    @PatchMapping("/{id}/teacher")
    public ResponseEntity<Course> updateCourseTeacher(@PathVariable int id, @RequestBody UpdateCourseTeacherDTO teacherDTO){
        Optional<Course> original = courseRepository.findById(id);
        if (!original.isPresent()){
            return ResponseEntity.notFound().build();
        }
        Course course = original.get();

        if (teacherDTO.getTeacherId() == null){
            course.setTeacher(null);
        } else {
            Optional<Teacher> teacher = teacherRepository.findById(teacherDTO.getTeacherId());
            if (!teacher.isPresent()){
                return ResponseEntity.notFound().build();
            }
            course.setTeacher(teacher.get());
        }
        courseRepository.save(course);
        return ResponseEntity.ok(course);
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
