package studentadmin.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import studentadmin.DTO.PatchStudentDTO;
import studentadmin.DTO.StudentDTO;
import studentadmin.models.House;
import studentadmin.models.Student;
import studentadmin.repositories.HouseRepository;
import studentadmin.services.StudentService;
import studentadmin.utils.Patcher;

import java.util.List;
import java.util.Optional;



@RestController
@RequestMapping("/students")
public class StudentController {

    private final Patcher patcher;
    private final StudentService studentService;


    public StudentController(Patcher patcher, StudentService studentService) {
        this.patcher = patcher;
        this.studentService = studentService;

    }

    @GetMapping
    public List<StudentDTO> getAllStudents(){
        List<StudentDTO> students = studentService.findAll();
        return students;
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudent(@PathVariable int id){
        Optional<StudentDTO> student = studentService.findById(id);
        return ResponseEntity.of(student);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Student> createStudent(@RequestBody StudentDTO studentDTO){
        Optional<Student> student = studentService.createStudent(studentDTO);
        return student.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable int id, @RequestBody Student student){
        return ResponseEntity.of(studentService.update(id, student));
    }


    @PatchMapping("/{id}")
    public ResponseEntity<Student> patchStudent(@PathVariable int id, @RequestBody PatchStudentDTO studentDTO){
        return ResponseEntity.of(studentService.patchStudent(id, studentDTO));
        } 

    @DeleteMapping("/{id}")
    public ResponseEntity<Student> deleteStudent(@PathVariable int id){
        return ResponseEntity.of(studentService.deleteById(id));
    }

}
