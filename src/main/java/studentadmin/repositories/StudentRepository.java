package studentadmin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import studentadmin.models.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {
}
