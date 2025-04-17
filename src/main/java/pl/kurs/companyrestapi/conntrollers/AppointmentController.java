package pl.kurs.companyrestapi.conntrollers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.companyrestapi.commands.CreateAppointmentCommand;
import pl.kurs.companyrestapi.commands.SearchAvailableAppointmentsCommand;
import pl.kurs.companyrestapi.exceptions.DoctorNotFound;
import pl.kurs.companyrestapi.exceptions.PatientNotFound;
import pl.kurs.companyrestapi.services.AppointmnetService;
import pl.kurs.companyrestapi.views.AppointmentSlotView;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/appointment")
@RequiredArgsConstructor
public class AppointmentController {

    private static final String APPOINTMENT_CREATED = "Appointment Successfully created";
    private final AppointmnetService appointmnetService;

    @PostMapping("/create")
    public ResponseEntity<String> createAppointment(@RequestBody CreateAppointmentCommand createAppointmentCommand) throws DoctorNotFound, PatientNotFound {
        appointmnetService.createAppointment(createAppointmentCommand);
        return ResponseEntity.status(HttpStatus.CREATED).body(APPOINTMENT_CREATED);
    }

    @GetMapping("/confirm/{token}")
    public ResponseEntity<Map<String, String>> confirmAppointment(@PathVariable String token) {
        try {
            appointmnetService.confirmAppointmentByToken(token);
            return ResponseEntity.ok(Map.of("message", "Wizyta potwierdzona!"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/cancel/{token}")
    public ResponseEntity<Map<String, String>> cancelAppointment(@PathVariable String token) {
        try {
            appointmnetService.cancelAppointmentByToken(token);
            return ResponseEntity.ok(Map.of("message", "Wizyta została anulowana"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/check")
    public ResponseEntity<List<AppointmentSlotView>> check(@RequestBody @Valid SearchAvailableAppointmentsCommand cmd) {
        List<AppointmentSlotView> result = appointmnetService.findAvailableSlots(cmd);
        return ResponseEntity.ok(result);
    }
}