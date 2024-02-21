package studentadmin.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import studentadmin.DTO.TeacherDTO;
import studentadmin.models.House;
import studentadmin.models.Teacher;
import studentadmin.repositories.HouseRepository;
import studentadmin.repositories.TeacherRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherRepository teacherRepository;
    private final HouseRepository houseRepository;

    public TeacherController(TeacherRepository teacherRepository, HouseRepository houseRepository) {
        this.teacherRepository = teacherRepository;
        this.houseRepository = houseRepository;
    }

    @GetMapping
    public List<Teacher> getAllTeachers(){
        List<Teacher> teachers = teacherRepository.findAll();
        return teachers;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Teacher> getTeacher(@PathVariable int id){
        Optional<Teacher> teacher = teacherRepository.findById(id);
        return ResponseEntity.of(teacher);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Teacher> createTeacher(@RequestBody TeacherDTO teacherDTO){
        Optional<House> house = houseRepository.findByName(teacherDTO.getHouse());
        if (!house.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Teacher teacher = new Teacher();
        teacher.setFirstName(teacherDTO.getFirstName());
        teacher.setMiddleName(teacherDTO.getMiddleName());
        teacher.setLastName(teacherDTO.getLastName());
        teacher.setDateOfBirth(teacherDTO.getDateOfBirth());
        teacher.setHouse(house.get());
        teacher.setHeadOfHouse(teacherDTO.isHeadOfHouse());
        teacher.setEmployment(teacherDTO.getEmployment());
        teacher.setEmploymentStart(teacherDTO.getEmploymentStart());
        teacher.setEmploymentEnd(teacherDTO.getEmploymentEnd());

        Teacher savedTeacher = teacherRepository.save(teacher);
        return ResponseEntity.ok().body(savedTeacher);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Teacher> updateTeacher(@PathVariable int id, @RequestBody Teacher teacher){
        Optional<Teacher> original = teacherRepository.findById(id);
        if (original.isPresent()) {
            Teacher originalTeacher = original.get();
            // Opdatér teacher
            originalTeacher.setFirstName(teacher.getFirstName());
            originalTeacher.setMiddleName(teacher.getMiddleName());
            originalTeacher.setLastName(teacher.getLastName());
            originalTeacher.setDateOfBirth(teacher.getDateOfBirth());
            originalTeacher.setHouse(teacher.getHouse());
            originalTeacher.setHeadOfHouse(teacher.isHeadOfHouse());
            originalTeacher.setEmployment(teacher.getEmployment());
            originalTeacher.setEmploymentStart(teacher.getEmploymentStart());
            originalTeacher.setEmploymentEnd(teacher.getEmploymentEnd());

            // Gem og returner opdaterede teacher
            Teacher updatedTeacher = teacherRepository.save(originalTeacher);
            return ResponseEntity.ok().body(updatedTeacher);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Teacher> deleteTeacher(@PathVariable int id){
        Optional<Teacher> teacher = teacherRepository.findById(id);
        teacherRepository.deleteById(id);
        return ResponseEntity.of(teacher);
    }
}
