package pl.kurs.companyrestapi.views;

import lombok.*;
import pl.kurs.companyrestapi.models.Patient;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class PatientView {

    private String animalName;
    private String animalSpecies;
    private String race;
    private Long age;
    private String firstName;
    private String lastName;
    private String email;


    public static PatientView buildPatientView(Patient patient) {
        return PatientView.builder()
                .animalName(patient.getAnimalName())
                .animalSpecies(patient.getAnimalSpecies())
                .race(patient.getRace())
                .age(patient.getAge())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .email(patient.getEmail())
                .build();
    }
}