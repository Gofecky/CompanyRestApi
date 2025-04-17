package pl.kurs.companyrestapi.conntrollers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.companyrestapi.commands.CreatePatientCommand;
import pl.kurs.companyrestapi.exceptions.PatientNotFound;
import pl.kurs.companyrestapi.services.PatientService;
import pl.kurs.companyrestapi.views.PatientListView;
import pl.kurs.companyrestapi.views.PatientView;

import java.util.List;

@RestController
@RequestMapping("/patient")
@RequiredArgsConstructor
public class PatientController {

    private static final String CREATED_PATIENT_MESSAGE = "Created";
    private final PatientService patientService;

    @PostMapping("/create")
    public ResponseEntity<String> createPatient(@Valid @RequestBody CreatePatientCommand createPatientCommand) {
        patientService.createPatient(createPatientCommand);
        return ResponseEntity.status(HttpStatus.CREATED).body(CREATED_PATIENT_MESSAGE);
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<PatientView> getPatientById(@PathVariable Long id) throws PatientNotFound {
        PatientView patientView = patientService.findPatientById(id);
        return ResponseEntity.ok(patientView);
    }

    @GetMapping("/patient-list")
    public List<PatientListView> getPatientList(@RequestParam int page, @RequestParam int size) {
        return patientService.getPatientsInList(page, size);
    }
}