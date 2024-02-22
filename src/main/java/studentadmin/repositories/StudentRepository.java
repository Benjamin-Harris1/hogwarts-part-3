package studentadmin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import studentadmin.models.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    @Query("SELECT s FROM Student s WHERE CONCAT(s.firstName, ' ', s.lastName) LIKE %?1%")
    List<Student> findByNameContaining(String name);
}
