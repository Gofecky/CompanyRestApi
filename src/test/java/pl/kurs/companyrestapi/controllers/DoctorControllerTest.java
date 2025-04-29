package pl.kurs.companyrestapi.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.kurs.companyrestapi.BaseTest;
import pl.kurs.companyrestapi.commands.CreateDoctorCommand;
import pl.kurs.companyrestapi.models.Doctor;
import pl.kurs.companyrestapi.repositories.DoctorRepository;
import pl.kurs.companyrestapi.views.DoctorListView;
import pl.kurs.companyrestapi.views.DoctorView;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DoctorControllerTest extends BaseTest {

    private static final String CREATE_DOCTOR_ENDPOINT = "/doctor/shouldAddDoctor";
    private static final String GET_BY_ID_DOCTOR_ENDPOINT = "/doctor/get-by-id/1";
    private static final String GET_DOCTOR_IN_LIST = "/doctor/get-doctors-in-list?page=0&size=30";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Captor
    private ArgumentCaptor<Doctor> argumentCaptor;
    @MockitoBean
    private DoctorRepository doctorRepository;

    @Test
    void shouldCreateDoctor() throws Exception {
        //given
        CreateDoctorCommand createDoctorCommand = CreateDoctorCommand.builder()
                .firstName("Jan")
                .lastName("Kowalski")
                .medicalSpecialization("Chirurgy")
                .animalSpecialization("Dogs")
                .hourSalary(BigDecimal.valueOf(90))
                .nip("1234567892")
                .build();
        when(doctorRepository.save(argumentCaptor.capture())).thenReturn(null);

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(CREATE_DOCTOR_ENDPOINT)
                        .content(mapper.writeValueAsString(createDoctorCommand))
                        .contentType("application/json"))
                .andExpect(status().is(201))
                .andReturn();

        //then
        assertThat(mvcResult.getResponse().getContentAsString()).isNotEmpty();

        Doctor resultDoctor = argumentCaptor.getValue();

        assertThat(resultDoctor.getFirstName()).isEqualTo(createDoctorCommand.getFirstName());
        assertThat(resultDoctor.getLastName()).isEqualTo(createDoctorCommand.getLastName());
        assertThat(resultDoctor.getMedicalSpecialization()).isEqualTo(createDoctorCommand.getMedicalSpecialization());
        assertThat(resultDoctor.getAnimalSpecialization()).isEqualTo(createDoctorCommand.getAnimalSpecialization());
        assertThat(resultDoctor.getHourSalary()).isEqualTo(createDoctorCommand.getHourSalary());
        assertThat(resultDoctor.getNip()).isEqualTo(createDoctorCommand.getNip());

    }
    @Test
    void shouldFindById() throws Exception {
        //given
        Doctor doctor = new Doctor(
                "Jan",
                "Kowalski",
                "chirurgy",
                "Dogs",
                BigDecimal.valueOf(90),
                "1234567890"
        );
        DoctorView expectedResult = DoctorView.buildDoctorView(doctor);
        when(doctorRepository.findById(any())).thenReturn(Optional.of(doctor));

        //when
        MvcResult mvcResult = mockMvc.perform(get(GET_BY_ID_DOCTOR_ENDPOINT)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        //then
        String contentAsString = mvcResult.getResponse().getContentAsString();
        DoctorView result = mapper.readValue(contentAsString, DoctorView.class);

        assertThat(result).isEqualTo(expectedResult);
    }
    @Test
    void shouldFindDoctorsInList() throws Exception {
        Doctor doctor1 = new Doctor(
                "Jan",
                "Kowalski",
                "chirurgy",
                "Dogs",
                BigDecimal.valueOf(90),
                "1234567890"
        );
        Doctor doctor2 = new Doctor(
                "Anna",
                "Nowak",
                "chirurgy",
                "Cats",
                BigDecimal.valueOf(85),
                "9876543210"
        );
        List<Doctor> doctorList = List.of(doctor1,doctor2);
        List<DoctorListView> expectedResult = List.of(DoctorListView.buildDoctorListView(doctor1),
                DoctorListView.buildDoctorListView(doctor2));
        Page<Doctor> doctorPage = new PageImpl<>(doctorList);
        when(doctorRepository.findAll(any(PageRequest.class))).thenReturn(doctorPage);

        //when
        MvcResult mvcResult = mockMvc.perform(get(GET_DOCTOR_IN_LIST)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        //then
        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<DoctorListView> resultList = mapper.readValue(contentAsString,
                new TypeReference<List<DoctorListView>>() {
                });
        assertThat(resultList).isNotEmpty();

        assertThat(expectedResult).isEqualTo(resultList);
    }

    @Test
    void shouldFireDoctor() throws Exception {
        // given
        Doctor doctor = new Doctor(
                "Jan",
                "Kowalski",
                "chirurgy",
                "Dogs",
                BigDecimal.valueOf(90),
                "1234567890"
        );

        Long doctorId = 1L;

        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
        when(doctorRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ArgumentCaptor<Doctor> argumentCaptor = ArgumentCaptor.forClass(Doctor.class);

        // when
        mockMvc.perform(put("/doctor/fire-doctor/" + doctorId))
                .andExpect(status().isOk());

        // then
        verify(doctorRepository).save(argumentCaptor.capture());
        Doctor savedDoctor = argumentCaptor.getValue();
        assertThat(savedDoctor.isFired()).isTrue();
    }

}