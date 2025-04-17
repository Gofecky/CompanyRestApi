package pl.kurs.companyrestapi.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String animalName;
    private String animalSpecies;
    private String race;
    private Long age;
    private String firstName;
    private String lastName;
    private String email;

    public Patient(String animalName, String animalSpecies, String race, Long age, String firstName, String lastName, String email) {
        this.animalName = animalName;
        this.animalSpecies = animalSpecies;
        this.race = race;
        this.age = age;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}