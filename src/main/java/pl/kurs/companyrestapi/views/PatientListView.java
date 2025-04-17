package pl.kurs.companyrestapi.views;

import lombok.*;
import pl.kurs.companyrestapi.models.Patient;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@EqualsAndHashCode
public class PatientListView {

    private String animalName;
    private String animalSpecies;
    private String race;
    private Long age;
    private String firstName;
    private String lastName;
    private String email;

    public static PatientListView fromPatient(Patient patient) {
        return PatientListView.builder().animalName(patient.getAnimalName()).animalSpecies(patient.getAnimalSpecies()).race(patient.getRace()).age(patient.getAge()).firstName(patient.getFirstName()).lastName(patient.getLastName()).email(patient.getEmail()).build();
    }
}