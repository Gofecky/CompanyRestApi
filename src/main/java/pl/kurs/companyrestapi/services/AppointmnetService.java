package pl.kurs.companyrestapi.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kurs.companyrestapi.commands.CreateAppointmentCommand;
import pl.kurs.companyrestapi.commands.SearchAvailableAppointmentsCommand;
import pl.kurs.companyrestapi.exceptions.DoctorNotFound;
import pl.kurs.companyrestapi.exceptions.PatientNotFound;
import pl.kurs.companyrestapi.models.Appointment;
import pl.kurs.companyrestapi.models.Doctor;
import pl.kurs.companyrestapi.models.Patient;
import pl.kurs.companyrestapi.repositories.AppointmentRepository;
import pl.kurs.companyrestapi.repositories.DoctorRepository;
import pl.kurs.companyrestapi.repositories.PatientRepository;
import pl.kurs.companyrestapi.views.AppointmentSlotView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AppointmnetService {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final EmailService emailService;

    public void createAppointment(CreateAppointmentCommand createAppointmentCommand) throws DoctorNotFound, PatientNotFound {
        Doctor doctor = doctorRepository.findById(createAppointmentCommand.doctorId).orElseThrow(DoctorNotFound::new);
        Patient patient = patientRepository.findById(createAppointmentCommand.patientId).orElseThrow(PatientNotFound::new);
        if (doctor.isFired()) {
            throw new IllegalStateException("Doctor has been fired");
        }
        if (appointmentRepository.existsByDoctorAndDate(doctor, createAppointmentCommand.date))
            throw new IllegalArgumentException("Doctor already has an appointment at this time");
        if (appointmentRepository.existsByPatientAndDate(patient, createAppointmentCommand.date))
            throw new IllegalArgumentException("Patient already has an appointment at this time");

        String token = UUID.randomUUID().toString();

        Appointment app = new Appointment();
        app.setDoctor(doctor);
        app.setPatient(patient);
        app.setDate(createAppointmentCommand.date);
        app.setToken(token);
        app.setConfirmed(false);
        app.setCreatedAt(LocalDateTime.now());

        appointmentRepository.save(app);
        emailService.sendAppointmentToken("gofecki@gmail.com", token);
    }

    public void confirmAppointmentByToken(String token) {
        Appointment appointment = appointmentRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Nieprawidłowy token"));

        if (appointment.isConfirmed()) {
            throw new IllegalArgumentException("Wizyta już została potwierdzona");
        }

        if (appointment.getCreatedAt().plusHours(1).isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Za późno na potwierdzenie wizyty");
        }

        appointment.setConfirmed(true);
        appointmentRepository.save(appointment);
    }

    public void cancelAppointmentByToken(String token) {
        Appointment appointment = appointmentRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Nieprawidłowy token – brak wizyty"));

        if (appointment.isConfirmed()) {
            throw new IllegalArgumentException("Nie można anulować potwierdzonej wizyty");
        }

        appointmentRepository.delete(appointment);
    }

    public List<AppointmentSlotView> findAvailableSlots(SearchAvailableAppointmentsCommand cmd) {
        List<Doctor> doctors = doctorRepository.findByMedicalSpecializationIgnoreCaseAndAnimalSpecializationIgnoreCaseAndIsFiredFalse(cmd.getType(), cmd.getAnimal());

        List<AppointmentSlotView> results = new ArrayList<>();

        for (Doctor doctor : doctors) {
            LocalDateTime current = cmd.getFrom();

            while (!current.isAfter(cmd.getTo())) {
                boolean busy = appointmentRepository.existsByDoctorAndDate(doctor, current);
                if (!busy) {
                    results.add(new AppointmentSlotView(
                            doctor.getId(),
                            doctor.getFirstName() + " " + doctor.getLastName(),
                            current
                    ));
                }
                current = current.plusHours(1);
            }
        }

        return results;
    }

}
