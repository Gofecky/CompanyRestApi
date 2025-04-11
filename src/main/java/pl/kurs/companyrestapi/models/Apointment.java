package pl.kurs.companyrestapi.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Apointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name="doctor_id",referencedColumnName = "id")
    private Doctor doctor;
    @OneToOne
    @JoinColumn(name="patient_id",referencedColumnName = "id")
    private Patient patient;
    private LocalDate apointmentDate;
}
