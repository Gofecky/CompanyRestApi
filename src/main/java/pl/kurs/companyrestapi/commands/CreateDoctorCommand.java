package pl.kurs.companyrestapi.commands;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.kurs.companyrestapi.models.Doctor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CreateDoctorCommand {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String medicalSpecialization;

    @NotBlank
    private String animalSpecialization;

    @NotNull
    @Positive
    private BigDecimal hourSalary;

    @NotBlank
    private String nip;

    public Doctor toDoctor() {
        return new Doctor(firstName, lastName, medicalSpecialization, animalSpecialization, hourSalary, nip);
    }
}