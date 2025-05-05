package pl.kurs.companyrestapi.services;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.kurs.companyrestapi.commands.CreateDoctorCommand;
import pl.kurs.companyrestapi.exceptions.DoctorNotFound;
import pl.kurs.companyrestapi.models.Doctor;
import pl.kurs.companyrestapi.repositories.DoctorRepository;
import pl.kurs.companyrestapi.views.DoctorListView;
import pl.kurs.companyrestapi.views.DoctorView;

import java.util.List;


@RequiredArgsConstructor
@Service

public class DoctorService {

    private final DoctorRepository doctorRepository;

    public void ShouldAddDoctor(CreateDoctorCommand createDoctorCommand) {
        Doctor doctor = createDoctorCommand.toDoctor();
        doctorRepository.save(doctor);
    }

    public DoctorView getDoctorById(Long id) throws DoctorNotFound {
        Doctor doctor = getDoctor(id);
        return DoctorView.buildDoctorView(doctor);
    }

    public Doctor getDoctor(Long id) throws DoctorNotFound {
        return doctorRepository.findById(id).orElseThrow(DoctorNotFound::new);
    }

    public List<DoctorListView> getDoctorsInList(int page, int size) {
        List<Doctor> doctorList = doctorRepository.findAll(PageRequest.of(page, size)).getContent();
        return doctorList.stream()
                .map(DoctorListView::buildDoctorListView)
                .toList();
    }


    public void fireDoctor(Long id) throws DoctorNotFound {
        Doctor doctor = getDoctor(id);
        doctor.setFired(true);
        doctorRepository.save(doctor);
    }
}