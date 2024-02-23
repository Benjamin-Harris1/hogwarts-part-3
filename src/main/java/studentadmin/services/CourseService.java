package studentadmin.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import studentadmin.DTO.AddStudentsToCourseDTO;
import studentadmin.DTO.UpdateCourseTeacherDTO;
import studentadmin.models.Course;
import studentadmin.models.Student;
import studentadmin.models.Teacher;
import studentadmin.repositories.CourseRepository;
import studentadmin.repositories.StudentRepository;
import studentadmin.repositories.TeacherRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final StudentService studentService;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    public CourseService(CourseRepository courseRepository, StudentService studentService, TeacherRepository teacherRepository, StudentRepository studentRepository) {
        this.courseRepository = courseRepository;
        this.studentService = studentService;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
    }

    public List<Course> findAll(){
        return courseRepository.findAll();
    }

    public Optional<Course> findById(int id){
        return courseRepository.findById(id);
    }

    public Optional<Teacher> getCourseTeacher(int id){
        return courseRepository.findById(id).map(Course::getTeacher);
    }

    public Optional<List<Student>> getCourseStudents(int id){
        return courseRepository.findById(id).map(Course::getStudents);
    }

    public ResponseEntity<Course> createCourse(Course course){
    Course savedCourse = courseRepository.save(course);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedCourse);
    }

    public ResponseEntity<?> addStudentsToCourse(int id, AddStudentsToCourseDTO studentsDTO){
        Optional<Course> original = courseRepository.findById(id);
        if (!original.isPresent()){
            return ResponseEntity.notFound().build();
        }

        // Henter course
        Course course = original.get();

        // Opretter nyt array som fundne students bliver tilføjet til
        List<Student> studentsToAdd = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();

        // Looper over students og matcher enten ID eller name til en tilsvarende student i DB, og tilføjer til studentsToAdd array
        for (AddStudentsToCourseDTO.StudentIdentifier studentIdentifier : studentsDTO.getStudents()) {
            if (studentIdentifier.getId() > 0) {
                studentService.findStudentById(studentIdentifier.getId()).ifPresent(student -> {
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

    public Optional<Course> updateCourse(int id, Course courseDetails) {
        Optional<Course> originalCourseOpt = courseRepository.findById(id);
        if (originalCourseOpt.isPresent()) {
            Course originalCourse = originalCourseOpt.get();
            // Opdater course med de nye værdier fra courseDetails
            originalCourse.setSubject(courseDetails.getSubject());
            originalCourse.setSchoolYear(courseDetails.getSchoolYear());
            originalCourse.setCurrent(courseDetails.isCurrent());
            originalCourse.setTeacher(courseDetails.getTeacher());
            originalCourse.setStudents(courseDetails.getStudents());

            // Gem og returner det opdaterede kursus
            Course updatedCourse = courseRepository.save(originalCourse);
            return Optional.of(updatedCourse);
        } else {
            return Optional.empty();
        }
    }

    public Optional<Course> updateCourseTeacher(int id, UpdateCourseTeacherDTO teacherDTO){
        Optional<Course> original = courseRepository.findById(id);
        if (!original.isPresent()){
            return Optional.empty();
        }
        Course course = original.get();

        if (teacherDTO.getTeacherId() == null){
            course.setTeacher(null);
        } else {
            Optional<Teacher> teacherOg = teacherRepository.findById(teacherDTO.getTeacherId());
            if (!teacherOg.isPresent()){
                return Optional.empty();
            }
            course.setTeacher(teacherOg.get());
        }

        Course updatedCourse = courseRepository.save(course);
        return Optional.of(updatedCourse);

    }

    public Optional<Course> deleteCourse(int id){
        Optional<Course> course = this.findById(id);
        courseRepository.deleteById(id);
        return course;
    }

    public ResponseEntity<?> removeStudentFromCourse(int courseId, int studentId) {
        Optional<Course> courseOpt = courseRepository.findById(courseId);
        if (!courseOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Course course = courseOpt.get();

        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (!studentOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Student student = studentOpt.get();

        if (!course.getStudents().remove(student)) {
            return ResponseEntity.badRequest().body("Student not enrolled in course");
        }
        courseRepository.save(course);
        return ResponseEntity.ok().build();
    }


}
