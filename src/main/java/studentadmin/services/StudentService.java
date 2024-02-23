package studentadmin.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import studentadmin.DTO.StudentDTO.StudentPatchRequest;
import studentadmin.DTO.StudentDTO.StudentRequestDTO;
import studentadmin.DTO.StudentDTO.StudentResponseDTO;
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

    public List<StudentResponseDTO> findAll() {
        return studentRepository.findAll().stream().map(this::toDTO).toList();
    }

    public Optional<StudentResponseDTO> findById(int id) {
        return studentRepository.findById(id).map(this::toDTO);
    }

    public Optional<Student> findStudentById(int id) {
        return studentRepository.findById(id);
    }
    
    // Update this to use Mapper
    public Optional<StudentResponseDTO> createStudent(StudentRequestDTO studentDTO){
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

        Student savedStudent = studentRepository.save(student);

        return Optional.of(toDTO(savedStudent));

    }

    public Optional<StudentResponseDTO> update(int id, StudentRequestDTO studentDTO) {
        Optional<Student> studentOptional = studentRepository.findById(id);
        if (!studentOptional.isPresent()) {
            return Optional.empty();
        }
        Student student = studentOptional.get();

        // Opdater den eksisterende student entitet med værdier fra DTO
        student = toEntity(studentDTO, student);
    
        Student updatedStudent = studentRepository.save(student);
    
        return Optional.of(toDTO(updatedStudent));
    }

    public Optional<StudentResponseDTO> patchStudent(int id, StudentPatchRequest studentDTO){
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

            Student savedStudent = studentRepository.save(student);

            // Konverter entity tilbage til DTO
           return Optional.of(toDTO(savedStudent)); 
        } catch(IllegalAccessException e) {
            e.printStackTrace();
            return Optional.empty();
    }
    }

    public Optional<StudentResponseDTO> deleteById(int id) {
        Optional<StudentResponseDTO> student = this.findById(id);
        studentRepository.deleteById(id);
        return student;
    }


    public StudentResponseDTO toDTO(Student entity){
        StudentResponseDTO dto = new StudentResponseDTO();
        dto.setId(entity.getId());
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

    private Student toEntity(StudentRequestDTO studentDTO, Student student) {
        if (studentDTO.getHouse() != null) {
            Optional<House> house = houseRepository.findByName(studentDTO.getHouse());
            house.ifPresent(student::setHouse);
        }
        student.setFirstName(studentDTO.getFirstName());
        student.setMiddleName(studentDTO.getMiddleName());
        student.setLastName(studentDTO.getLastName());
        student.setDateOfBirth(studentDTO.getDateOfBirth());
        student.setPrefect(studentDTO.isPrefect());
        student.setEnrollmentYear(studentDTO.getEnrollmentYear());
        student.setGraduationYear(studentDTO.getGraduationYear());
        student.setGraduated(studentDTO.isGraduated());
        student.setSchoolYear(studentDTO.getSchoolYear());
        return student;
    }
}
