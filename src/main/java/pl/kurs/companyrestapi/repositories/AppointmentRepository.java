package pl.kurs.companyrestapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kurs.companyrestapi.models.Appointment;
import pl.kurs.companyrestapi.models.Doctor;
import pl.kurs.companyrestapi.models.Patient;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    boolean existsByDoctorAndDate(Doctor doctor, LocalDateTime date);

    boolean existsByPatientAndDate(Patient patient, LocalDateTime date);

    Optional<Appointment> findByToken(String token);

    int deleteAllByConfirmedFalseAndCreatedAtBefore(LocalDateTime dateTime);
}