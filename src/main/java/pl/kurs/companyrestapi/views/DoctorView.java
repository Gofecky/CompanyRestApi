package pl.kurs.companyrestapi.views;

import lombok.*;
import pl.kurs.companyrestapi.models.Doctor;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class DoctorView {

    private String firstName;
    private String lastName;
    private String medicalSpecialization;
    private String animalSpecialization;
    private BigDecimal hourSalary;
    private String nip;
    private boolean isFired;

    public static DoctorView buildDoctorView(Doctor doctor) {
        return DoctorView.builder()
                .firstName(doctor.getFirstName())
                .lastName(doctor.getLastName())
                .medicalSpecialization(doctor.getMedicalSpecialization())
                .animalSpecialization(doctor.getAnimalSpecialization())
                .hourSalary(doctor.getHourSalary())
                .nip(doctor.getNip())
                .isFired(doctor.isFired())
                .build();
    }
}