package pl.kurs.companyrestapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.companyrestapi.models.Doctor;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findByMedicalSpecializationIgnoreCaseAndAnimalSpecializationIgnoreCaseAndIsFiredFalse(
            String type, String animalSpecialization);
}