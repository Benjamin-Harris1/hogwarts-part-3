package studentadmin;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import studentadmin.models.*;
import studentadmin.repositories.CourseRepository;
import studentadmin.repositories.HouseRepository;
import studentadmin.repositories.StudentRepository;
import studentadmin.repositories.TeacherRepository;

@Component
public class InitData implements CommandLineRunner {

    @Autowired
    private final CourseRepository courseRepository;
    private final HouseRepository houseRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    public InitData(CourseRepository courseRepository, HouseRepository houseRepository, StudentRepository studentRepository, TeacherRepository teacherRepository) {
        this.courseRepository = courseRepository;
        this.houseRepository = houseRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }

    public void run(String... args){
        System.out.println("System is running");


        // Opretter houses
        House gryffindor = new House("Gryffindor", "Godric Gryffindor", Arrays.asList("red", "gold"));
        House hufflepuff = new House("Hufflepuff", "Helga Hufflepuff", Arrays.asList("yellow", "black"));
        House ravenclaw = new House("Ravenclaw", "Rowena Ravenclaw", Arrays.asList("blue", "silver"));
        House slytherin = new House("Slytherin", "Salazar Slytherin", Arrays.asList("green", "silver"));
        houseRepository.saveAll(List.of(gryffindor, hufflepuff, ravenclaw, slytherin));

        // Opretter teachers
        Teacher mcgonagall = new Teacher("Minerva", null, "McGonagall", LocalDate.parse("1881-07-01"), gryffindor, true, EmpType.TENURED, LocalDate.parse("1970-01-01"), null);
        Teacher snape = new Teacher("Severus", null, "Snape", LocalDate.parse("1960-01-01"), slytherin, true, EmpType.TENURED, LocalDate.parse("1975-01-01"), null);
        teacherRepository.saveAll(List.of(mcgonagall, snape));

        // Opretter students
        Student harry = new Student("Harry", "James", "Potter", LocalDate.parse("1980-07-31"), gryffindor, true, 1991, 1998, true);
        Student hermione = new Student("Hermione", "Jean", "Granger", LocalDate.parse("1979-09-19"), gryffindor, false, 1991, 1998, true);
        Student ron = new Student("Ronald", "Bilius", "Weasley", LocalDate.parse("1980-03-01"), gryffindor, false, 1991, 1998, true);
        Student draco = new Student("Draco", "Lucius", "Malfoy", LocalDate.parse("1980-06-05"), slytherin, false, 1991, 1998, true);
        Student luna = new Student("Luna", "", "Lovegood", LocalDate.parse("1981-02-13"), ravenclaw, false, 1992, 1999, true);
        Student neville = new Student("Neville", "", "Longbottom", LocalDate.parse("1980-07-30"), gryffindor, false, 1991, 1998, true);
        Student ginny = new Student("Ginny", "", "Weasley", LocalDate.parse("1981-08-11"), gryffindor, false, 1992, 1999, true);
        Student fred = new Student("Fred", "", "Weasley", LocalDate.parse("1978-04-01"), gryffindor, false, 1989, 1996, true);
        Student george = new Student("George", "", "Weasley", LocalDate.parse("1978-04-01"), gryffindor, false, 1989, 1996, true);
        Student cedric = new Student("Cedric", "", "Diggory", LocalDate.parse("1977-09-01"), hufflepuff, true, 1989, 1995, false);
        Student cho = new Student("Cho", "", "Chang", LocalDate.parse("1979-01-01"), ravenclaw, false, 1990, 1997, true);
        Student seamus = new Student("Seamus", "", "Finnigan", LocalDate.parse("1980-01-01"), gryffindor, false, 1991, 1998, true);
        Student dean = new Student("Dean", "", "Thomas", LocalDate.parse("1980-01-01"), gryffindor, false, 1991, 1998, true);
        Student padma = new Student("Padma", "", "Patil", LocalDate.parse("1979-01-01"), ravenclaw, false, 1991, 1998, true);
        Student parvati = new Student("Parvati", "", "Patil", LocalDate.parse("1979-01-01"), gryffindor, false, 1991, 1998, true);
        studentRepository.saveAll(List.of(harry, hermione, ron, draco, luna, neville, ginny, fred, george, cedric, cho, seamus, dean, padma, parvati));

        // Opretter course
        Course transfiguration = new Course("Transfiguration", 1991, true, mcgonagall, null);
        transfiguration.setStudents(List.of(harry, hermione, ron, draco, luna, neville, ginny, fred, george, cedric, cho, seamus, dean, padma, parvati));
        courseRepository.saveAll(List.of(transfiguration));

    }
}
