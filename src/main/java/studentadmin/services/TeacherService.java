package studentadmin.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import studentadmin.DTO.TeacherDTO.TeacherPatchDTO;
import studentadmin.DTO.TeacherDTO.TeacherDTO;
import studentadmin.models.House;
import studentadmin.models.Teacher;
import studentadmin.repositories.HouseRepository;
import studentadmin.repositories.TeacherRepository;
import studentadmin.utils.Patcher;

import java.util.List;
import java.util.Optional;


@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final HouseRepository houseRepository;
    private final Patcher patcher;

    @Autowired
    public TeacherService(TeacherRepository teacherRepository, HouseRepository houseRepository, Patcher patcher) {
        this.teacherRepository = teacherRepository;
        this.houseRepository = houseRepository;
        this.patcher = patcher;
    }


    public List<TeacherDTO> findAll() {
        return teacherRepository.findAll().stream().map(this::toDTO).toList();
    }

    public Optional<TeacherDTO> findById(int id) {
        return teacherRepository.findById(id).map(this::toDTO);
    }

    public Optional<TeacherDTO> create(TeacherDTO teacherDTO){
        Teacher teacher = toEntity(teacherDTO, new Teacher());
        teacher = teacherRepository.save(teacher);
        return Optional.of(toDTO(teacher));
    }

    public Optional<TeacherDTO> updateTeacher(int id, TeacherDTO teacherDTO){
        Optional<Teacher> teacherOptional = teacherRepository.findById(id);
        if (teacherOptional.isPresent()){
            Teacher teacher = toEntity(teacherDTO, teacherOptional.get());
            teacher = teacherRepository.save(teacher);
            return Optional.of(toDTO(teacher));
        }
        return Optional.empty();
    }

    public Optional<TeacherDTO> patchTeacher(int id, TeacherPatchDTO teacherDTO){
        Optional<Teacher> original = teacherRepository.findById(id);
        if (!original.isPresent()){
            return Optional.empty();
        }
        Teacher teacher = original.get();
        try {
            // Sender begge objekter til patcher klasse
            patcher.patchObject(teacher, teacherDTO);

            // Tjekker om employmentEnd er sat til null med vilje
            if (teacherDTO.getEmploymentEnd() == null && teacherDTO.isEmploymentEndUpdated()){
                teacher.setEmploymentEnd(null);
            }
            teacher = teacherRepository.save(teacher);
            return Optional.of(toDTO(teacher));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<TeacherDTO> deleteById(int id) {
        Optional<TeacherDTO> teacher = this.findById(id);
        teacherRepository.deleteById(id);
        return teacher;
    }

    private TeacherDTO toDTO(Teacher entity){
        TeacherDTO dto = new TeacherDTO();
        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setMiddleName(entity.getMiddleName());
        dto.setLastName(entity.getLastName());
        dto.setDateOfBirth(entity.getDateOfBirth());
        dto.setHouse(entity.getHouse().getName());
        dto.setHeadOfHouse(entity.isHeadOfHouse());
        dto.setEmployment(entity.getEmployment());
        dto.setEmploymentStart(entity.getEmploymentStart());
        dto.setEmploymentEnd(entity.getEmploymentEnd());
        return dto;
    }

    private Teacher toEntity(TeacherDTO dto, Teacher teacher){
        if (dto.getHouse() != null) {
            Optional<House> house = houseRepository.findByName(dto.getHouse());
            house.ifPresent(teacher::setHouse);
        }
        teacher.setFirstName(dto.getFirstName());
        teacher.setMiddleName(dto.getMiddleName());
        teacher.setLastName(dto.getLastName());
        teacher.setDateOfBirth(dto.getDateOfBirth());
        teacher.setHeadOfHouse(dto.isHeadOfHouse());
        teacher.setEmployment(dto.getEmployment());
        teacher.setEmploymentStart(dto.getEmploymentStart());
        teacher.setEmploymentEnd(dto.getEmploymentEnd());
        return teacher;
        
    }
}
