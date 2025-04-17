package pl.kurs.companyrestapi.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.kurs.companyrestapi.commands.CreatePatientCommand;
import pl.kurs.companyrestapi.exceptions.PatientNotFound;
import pl.kurs.companyrestapi.models.Patient;
import pl.kurs.companyrestapi.repositories.PatientRepository;
import pl.kurs.companyrestapi.views.PatientListView;
import pl.kurs.companyrestapi.views.PatientView;

import java.util.List;

@RequiredArgsConstructor
@Service

public class PatientService {

    private final PatientRepository patientRepository;

    public void createPatient(CreatePatientCommand createPatientCommand) {

        Patient patient = createPatientCommand.toPatient();
        patientRepository.save(patient);
    }

    public PatientView findPatientById(Long id) throws PatientNotFound {
        Patient patient = patientRepository.findById(id).orElseThrow(PatientNotFound::new);
        return PatientView.buildPatientView(patient);
    }

    public List<PatientListView> getPatientsInList(int page, int size) {
        List<Patient> patientsList = patientRepository.findAll(PageRequest.of(page, size)).getContent();
        return patientsList.stream()
                .map((PatientListView::fromPatient))
                .toList();
    }
}