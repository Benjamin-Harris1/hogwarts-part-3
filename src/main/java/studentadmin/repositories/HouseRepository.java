package studentadmin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import studentadmin.models.House;

import java.util.Optional;

public interface HouseRepository extends JpaRepository<House, Integer> {
    Optional<House> findByName(String name);
}
