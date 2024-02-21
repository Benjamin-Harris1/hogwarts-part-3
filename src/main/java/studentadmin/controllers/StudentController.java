package studentadmin.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import studentadmin.DTO.StudentDTO;
import studentadmin.models.House;
import studentadmin.models.Student;
import studentadmin.repositories.HouseRepository;
import studentadmin.repositories.StudentRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentRepository studentRepository;
    private final HouseRepository houseRepository;

    public StudentController(StudentRepository studentRepository, HouseRepository houseRepository) {
        this.studentRepository = studentRepository;
        this.houseRepository = houseRepository;
    }

    @GetMapping
    public List<Student> getAllStudents(){
        List<Student> students = studentRepository.findAll();
        return students;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable int id){
        Optional<Student> student = studentRepository.findById(id);
        return ResponseEntity.of(student);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Student> createStudent(@RequestBody StudentDTO studentDTO){
        Optional<House> house = houseRepository.findByName(studentDTO.getHouse());
        // Hvis house med angivne navn ikke findes, returneres 404 not found
        if (!house.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        // Tjekker om fullname er angivet og opdeler hvis ja
        if (studentDTO.getFullName() != null) {
            studentDTO.setFullName(studentDTO.getFullName());
        }

        Student student = new Student();
        student.setFirstName(studentDTO.getFirstName());
        student.setMiddleName(studentDTO.getMiddleName());
        student.setLastName(studentDTO.getLastName());
        student.setDateOfBirth(studentDTO.getDateOfBirth());
        student.setHouse(house.get());
        student.setPrefect(studentDTO.isPrefect());
        student.setEnrollmentYear(studentDTO.getEnrollmentYear());
        student.setGraduationYear(studentDTO.getGraduationYear());
        student.setGraduated(studentDTO.isGraduated());

        Student savedStudent = studentRepository.save(student);
        return ResponseEntity.ok().body(savedStudent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable int id, @RequestBody Student student){
        Optional<Student> original = studentRepository.findById(id);
        if (original.isPresent()) {
            Student originalStudent = original.get();
            // Opdatér student
            originalStudent.setFirstName(student.getFirstName());
            originalStudent.setMiddleName(student.getMiddleName());
            originalStudent.setLastName(student.getLastName());
            originalStudent.setDateOfBirth(student.getDateOfBirth());
            originalStudent.setHouse(student.getHouse());
            originalStudent.setEnrollmentYear(student.getEnrollmentYear());
            originalStudent.setGraduationYear(student.getGraduationYear());
            originalStudent.setPrefect(student.isPrefect());
            originalStudent.setGraduated(student.isGraduated());

            // Gem og returner opdaterede student
            Student updatedStudent = studentRepository.save(originalStudent);
            return ResponseEntity.ok().body(updatedStudent);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Student> deleteStudent(@PathVariable int id){
        Optional<Student> student = studentRepository.findById(id);
        studentRepository.deleteById(id);
        return ResponseEntity.of(student);
    }

}
