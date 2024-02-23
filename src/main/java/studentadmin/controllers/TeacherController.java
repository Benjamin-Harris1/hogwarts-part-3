package studentadmin.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import studentadmin.DTO.TeacherDTO.TeacherPatchDTO;
import studentadmin.DTO.TeacherDTO.TeacherDTO;
import studentadmin.services.TeacherService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherService teacherService;


    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping
    public List<TeacherDTO> getAllTeachers(){
        List<TeacherDTO> teachers = teacherService.findAll();
        return teachers;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherDTO> getTeacher(@PathVariable int id){
        Optional<TeacherDTO> teacher = teacherService.findById(id);
        return ResponseEntity.of(teacher);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TeacherDTO> createTeacher(@RequestBody TeacherDTO teacherDTO){
        Optional<TeacherDTO> teacher = teacherService.create(teacherDTO);
        return teacher.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeacherDTO> updateTeacher(@PathVariable int id, @RequestBody TeacherDTO teacherDTO){
        return ResponseEntity.of(teacherService.updateTeacher(id, teacherDTO));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TeacherDTO> patchTeacher(@PathVariable int id, @RequestBody TeacherPatchDTO teacherDTO){
        return ResponseEntity.of(teacherService.patchTeacher(id, teacherDTO));
    }
    

    @DeleteMapping("/{id}")
    public ResponseEntity<TeacherDTO> deleteTeacher(@PathVariable int id){
        return ResponseEntity.of(teacherService.deleteById(id));
    }
}
