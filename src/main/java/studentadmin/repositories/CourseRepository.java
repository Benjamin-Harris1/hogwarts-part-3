package studentadmin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import studentadmin.models.Course;

public interface CourseRepository extends JpaRepository<Course, Integer> {
}
