package pl.kurs.companyrestapi.views;

import lombok.*;
import pl.kurs.companyrestapi.models.Doctor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@EqualsAndHashCode
public class DoctorListView {

    private String firstName;
    private String lastName;
    private String medicalSpecialization;
    private String animalSpecialization;
    private BigDecimal hourSalary;
    private String nip;

    public static DoctorListView buildDoctorListView(Doctor doctor) {
        return DoctorListView.builder()
                .firstName(doctor.getFirstName())
                .lastName(doctor.getLastName())
                .medicalSpecialization(doctor.getMedicalSpecialization())
                .animalSpecialization(doctor.getAnimalSpecialization())
                .hourSalary(doctor.getHourSalary())
                .nip(doctor.getNip())
                .build();
    }

}