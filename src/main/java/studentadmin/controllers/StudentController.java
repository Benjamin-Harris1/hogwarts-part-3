package studentadmin.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import studentadmin.DTO.PatchStudentDTO;
import studentadmin.DTO.StudentRequestDTO;
import studentadmin.DTO.StudentResponseDTO;
import studentadmin.models.Student;
import studentadmin.services.StudentService;

import java.util.List;
import java.util.Optional;



@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;


    public StudentController(StudentService studentService) {
        this.studentService = studentService;

    }

    @GetMapping
    public List<StudentResponseDTO> getAllStudents(){
        List<StudentResponseDTO> students = studentService.findAll();
        return students;
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> getStudent(@PathVariable int id){
        Optional<StudentResponseDTO> student = studentService.findById(id);
        return ResponseEntity.of(student);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Student> createStudent(@RequestBody StudentRequestDTO studentDTO){
        Optional<Student> student = studentService.createStudent(studentDTO);
        return student.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> updateStudent(@PathVariable int id, @RequestBody Student student){
        return ResponseEntity.of(studentService.update(id, student));
    }


    @PatchMapping("/{id}")
    public ResponseEntity<Student> patchStudent(@PathVariable int id, @RequestBody PatchStudentDTO studentDTO){
        return ResponseEntity.of(studentService.patchStudent(id, studentDTO));
        } 

    @DeleteMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> deleteStudent(@PathVariable int id){
        return ResponseEntity.of(studentService.deleteById(id));
    }

}
