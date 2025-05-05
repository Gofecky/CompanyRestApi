package pl.kurs.companyrestapi.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kurs.companyrestapi.commands.CreateDoctorCommand;
import pl.kurs.companyrestapi.exceptions.DoctorNotFound;
import pl.kurs.companyrestapi.models.Doctor;
import pl.kurs.companyrestapi.repositories.DoctorRepository;
import pl.kurs.companyrestapi.views.DoctorView;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class DoctorServiceTest {
    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private DoctorService doctorService;

    @Test
    void shouldSaveDoctorBasedOnCreateDoctorCommand() {
        // Given
        CreateDoctorCommand createDoctorCommand = CreateDoctorCommand.builder()
                .firstName("Jan")
                .lastName("Kowalski")
                .nip("1234567890")
                .hourSalary(BigDecimal.valueOf(65))
                .animalSpecialization("Dogs")
                .medicalSpecialization("Chirurgy")
                .build();

        Doctor expectedDoctor = createDoctorCommand.toDoctor();

        // When
        doctorService.ShouldAddDoctor(createDoctorCommand);

        // Then
        then(doctorRepository).should().save(refEq(expectedDoctor));
    }
    @Test
    void shouldReturnDoctorViewWhenDoctorExists() throws DoctorNotFound {
        // Given
        Long doctorId = 1L;
        Doctor doctor = new Doctor("Jan", "Kowalski", "Chirurgy", "Dogs", BigDecimal.valueOf(65), "1234567890");
        doctor.setId(doctorId);
        DoctorView expectedView = DoctorView.buildDoctorView(doctor);

        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));

        // When
        DoctorView result = doctorService.getDoctorById(doctorId);

        // Then
        assertThat(result).usingRecursiveComparison().isEqualTo(expectedView);
    }
    @Test
    void shouldReturnDoctorWhenExists() throws DoctorNotFound {
        // Given
        Long doctorId = 1L;
        Doctor doctor = new Doctor("Jan", "Kowalski", "Chirurgy", "Dogs", BigDecimal.valueOf(65), "1234567890");
        doctor.setId(doctorId);
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));

        // When
        Doctor result = doctorService.getDoctor(doctorId);

        // Then
        assertThat(result).isEqualTo(doctor);
    }
    @Test
    void shouldSetFiredToTrueAndSaveDoctorWhenExists() throws DoctorNotFound {
        // Given
        Long doctorId = 1L;
        Doctor doctor = new Doctor("Jan", "Kowalski", "Chirurgy", "Dogs", BigDecimal.valueOf(65), "1234567890");
        doctor.setId(doctorId);
        doctor.setFired(false);
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));

        // When
        doctorService.fireDoctor(doctorId);

        // Then
        assertThat(doctor.isFired()).isTrue();
        verify(doctorRepository).save(doctor);
    }

}
