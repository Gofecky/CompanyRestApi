package pl.kurs.companyrestapi.commands;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.kurs.companyrestapi.models.Patient;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CreatePatientCommand {
    @NotBlank
    private String animalName;
    @NotBlank
    private String animalSpecies;
    @NotBlank
    private String race;
    @NotNull
    @Positive
    private long age;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String email;

    public Patient toPatient() {

        return new Patient(animalName, animalSpecies, race, age, firstName, lastName, email);
    }
}