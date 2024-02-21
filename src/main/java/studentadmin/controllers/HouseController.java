package studentadmin.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import studentadmin.models.House;
import studentadmin.repositories.HouseRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/houses")
public class HouseController {
    private final HouseRepository houseRepository;


    public HouseController(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }

    @GetMapping
    public List<House> getAllHouses(){
        List<House> houses = houseRepository.findAll();
        return houses;
    }

    @GetMapping("/{name}")
    public ResponseEntity<House> getHouseByName(@PathVariable String name){
        Optional<House> house = houseRepository.findByName(name);
        return ResponseEntity.of(house);

    }

}
