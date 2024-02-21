package studentadmin.models;

import jakarta.persistence.*;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
public class House {
    @Id
    private String name;
    private String founder;

    // Elementcollection bruges for at normalisere db (måske lidt overkill)
    @ElementCollection
    private List<String> colors;

    public House(String name, String founder, List<String> colors) {
        this.name = name;
        this.founder = founder;
        this.colors = colors;
    }

    public House() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFounder() {
        return founder;
    }

    public void setFounder(String founder) {
        this.founder = founder;
    }

    public List<String> getColors() {
        return colors;
    }

    // Sikrer at der kun kan forekomme PRÆCIST 2 color-værdier
    public void setColors(List<String> colors) {
        if (colors != null && colors.size() == 2) {
            this.colors = colors;
        } else {
            throw new IllegalArgumentException("There needs to be exactly 2 colors");
        }
    }
}
