package studentadmin.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import studentadmin.models.House;
import studentadmin.repositories.HouseRepository;

import java.util.List;

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

}
