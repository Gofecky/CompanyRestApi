package pl.kurs.companyrestapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.companyrestapi.models.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}