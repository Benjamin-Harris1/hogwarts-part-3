package studentadmin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import studentadmin.models.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
}
