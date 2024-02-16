package studentadmin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import studentadmin.models.House;

public interface HouseRepository extends JpaRepository<House, Integer> {
}
