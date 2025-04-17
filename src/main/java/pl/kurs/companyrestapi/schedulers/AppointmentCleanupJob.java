package pl.kurs.companyrestapi.schedulers;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.kurs.companyrestapi.repositories.AppointmentRepository;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AppointmentCleanupJob {

    private final AppointmentRepository appointmentRepository;

    @Transactional
    @Scheduled(fixedRate = 60 * 1000)
    public void cleanUpUnconfirmedAppointments() {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        int deleted = appointmentRepository.deleteAllByConfirmedFalseAndCreatedAtBefore(oneHourAgo);
        if (deleted > 0) {
            System.out.println("Usunięto " + deleted + " niepotwierdzonych wizyt starszych niż 1h.");
        }
    }
}