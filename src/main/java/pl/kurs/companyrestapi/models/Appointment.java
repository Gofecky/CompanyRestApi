package pl.kurs.companyrestapi.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    private LocalDateTime createdAt;
    @Column(unique = true, nullable = false)
    private String token;
    private boolean confirmed = false;
    private LocalDateTime date;


    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}