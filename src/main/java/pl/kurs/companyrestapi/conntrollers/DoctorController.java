package pl.kurs.companyrestapi.conntrollers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.companyrestapi.commands.CreateDoctorCommand;
import pl.kurs.companyrestapi.commands.FireDoctorCommand;
import pl.kurs.companyrestapi.exceptions.DoctorNotFound;
import pl.kurs.companyrestapi.services.DoctorService;
import pl.kurs.companyrestapi.views.DoctorListView;
import pl.kurs.companyrestapi.views.DoctorView;

import java.util.List;

@RestController
@RequestMapping("/doctor")
@RequiredArgsConstructor
public class DoctorController {

    private static final String DOCTOR_SUCCESSFULLY_CREATED = "Created";
    private static final String DOCTOR_SUCCESSFULLY_FIRED = "changed status of given doctor, this doctor will not be able to handle any visits.";
    private final DoctorService doctorService;

    @PostMapping("/shouldAddDoctor")
    public ResponseEntity<String> shouldAddDoctor(@Valid @RequestBody CreateDoctorCommand createDoctorCommand) {
        doctorService.ShouldAddDoctor(createDoctorCommand);
        return ResponseEntity.status(HttpStatus.CREATED).body(DOCTOR_SUCCESSFULLY_CREATED);
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<DoctorView> findById(@PathVariable Long id) throws DoctorNotFound {
        DoctorView doctorById = doctorService.getDoctorById(id);
        return ResponseEntity.ok(doctorById);
    }

    @GetMapping("/get-doctors-in-list")
    public List<DoctorListView> getDoctorsInList(@RequestParam int page, @RequestParam int size) {
        return doctorService.getDoctorsInList(page, size);
    }

    @PutMapping("/fire-doctor/{id}")
    ResponseEntity<String> fireDoctor(@RequestBody FireDoctorCommand fireDoctorCommand, @PathVariable Long id) throws DoctorNotFound {
        doctorService.fireDoctor(fireDoctorCommand, id);
        return ResponseEntity.ok(DOCTOR_SUCCESSFULLY_FIRED);
    }
}