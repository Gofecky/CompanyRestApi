package pl.kurs.companyrestapi.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String medicalSpecialization;
    private String animalSpecialization;
    private BigDecimal hourSalary;
    @Column(unique = true, nullable = false)
    private String nip;
    @Column(name = "is_fired")
    private boolean isFired;

    public boolean isFired() {
        return isFired;
    }

    public Doctor(String firstName, String lastName, String medicalSpecialization, String animalSpecialization, BigDecimal hourSalary, String nip) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.medicalSpecialization = medicalSpecialization;
        this.animalSpecialization = animalSpecialization;
        this.hourSalary = hourSalary;
        this.nip = nip;
        this.isFired = false;
    }
}