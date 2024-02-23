package studentadmin.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import studentadmin.DTO.PatchStudentDTO;
import studentadmin.DTO.StudentDTO;
import studentadmin.models.House;
import studentadmin.models.Student;
import studentadmin.repositories.HouseRepository;
import studentadmin.repositories.StudentRepository;
import studentadmin.utils.Patcher;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final HouseRepository houseRepository;
    private final Patcher patcher;

    @Autowired
    public StudentService(StudentRepository studentRepository, HouseRepository houseRepository, Patcher patcher) {
        this.studentRepository = studentRepository;
        this.houseRepository = houseRepository;
        this.patcher = patcher;
    }


    public List<Student> findStudentsByName(String name) {
        return studentRepository.findByNameContaining(name);
    }

    public List<StudentDTO> findAll() {
        return studentRepository.findAll().stream().map(this::toDTO).toList();
    }

    public Optional<StudentDTO> findById(int id) {
        return studentRepository.findById(id).map(this::toDTO);
    }

    public Student save(Student student) {
        return studentRepository.save(student);
    }

    public Optional<Student> createStudent(StudentDTO studentDTO){
        Optional<House> house = houseRepository.findByName(studentDTO.getHouse());
        if (!house.isPresent()) {
            return Optional.empty();
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
        student.setSchoolYear(studentDTO.getSchoolYear());
        return Optional.of(studentRepository.save(student));

    }

    public Optional<Student> update(int id, Student student) {
        Optional<Student> studentOptional = studentRepository.findById(id);
        if (studentOptional.isPresent()) {
            student.setId(id);
            studentRepository.save(student);
        }
        return studentOptional;
    }

    public Optional<Student> patchStudent(int id, PatchStudentDTO studentDTO){
        Optional<Student> original = studentRepository.findById(id);
        if (!original.isPresent()) {
            return Optional.empty();
        }
        Student student = original.get();
        try {
            // Send both obhect to the patcher
            patcher.patchObject(student, studentDTO);

            // Tjekker om graduationYear er sat til null med vilje
            if (studentDTO.getGraduationYear() == null && studentDTO.isGraduationYearUpdated()) {
                student.setGraduationYear(null);
                student.setGraduated(false);
            }
            // Tjekker om graduationYear er sat, og sætter graduated til true
            if (studentDTO.getGraduationYear() != null ) {
                student.setGraduated(true);
            }

           return Optional.of(studentRepository.save(student)); 
        } catch(IllegalAccessException e) {
            e.printStackTrace();
            return Optional.empty();
    }
    }

    public Optional<Student> deleteById(int id) {
        Optional<Student> student = studentRepository.findById(id);
        studentRepository.deleteById(id);
        return student;
    }


    public StudentDTO toDTO(Student entity){
        StudentDTO dto = new StudentDTO();
        dto.setFirstName(entity.getFirstName());
        dto.setMiddleName(entity.getMiddleName());
        dto.setLastName(entity.getLastName());
        dto.setDateOfBirth(entity.getDateOfBirth());
        dto.setHouse(entity.getHouse().getName());
        dto.setPrefect(entity.isPrefect());
        dto.setEnrollmentYear(entity.getEnrollmentYear());
        dto.setGraduationYear(entity.getGraduationYear());
        dto.setGraduated(entity.isGraduated());
        dto.setSchoolYear(entity.getSchoolYear());
        return dto;
    }
}
