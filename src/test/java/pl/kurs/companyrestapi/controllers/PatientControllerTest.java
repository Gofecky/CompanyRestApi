package pl.kurs.companyrestapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.LifecycleState;
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
import pl.kurs.companyrestapi.commands.CreatePatientCommand;
import pl.kurs.companyrestapi.models.Patient;
import pl.kurs.companyrestapi.repositories.PatientRepository;
import pl.kurs.companyrestapi.views.PatientListView;
import pl.kurs.companyrestapi.views.PatientView;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PatientControllerTest extends BaseTest {

    private static final String CREATE_PATIENT_ENDPOINT ="/patient/create" ;
    private static final String GET_BY_ID_PATIENT_ENDPOINT = "/patient/get-by-id/1";
    private static final String GET_PATIENTS_IN_LIST = "/patient/patient-list?page=0&size=30";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @Captor
    private ArgumentCaptor<Patient> argumentCaptor;
    @MockitoBean
    private PatientRepository patientRepository;


    @Test
    void shouldCreateAndSavePatient() throws Exception {
        //given
        CreatePatientCommand createPatientCommand = CreatePatientCommand.builder()
                .firstName("Jan")
                .animalName("Jack")
                .animalSpecies("Dog")
                .race("buldog")
                .age(12L)
                .lastName("Kowalski")
                .email("gofecki@gmail.com")
                .build();
        when(patientRepository.save(argumentCaptor.capture())).thenReturn(null);

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(CREATE_PATIENT_ENDPOINT)
                        .content(mapper.writeValueAsString(createPatientCommand))
                        .contentType("application/json"))
                .andExpect(status().is(201))
                .andReturn();

        //then
        Assertions.assertThat(mvcResult.getResponse().getContentAsString()).isNotEmpty();

        Patient resultPatient = argumentCaptor.getValue();

        Assertions.assertThat(resultPatient.getAge()).isEqualTo(createPatientCommand.getAge());
        Assertions.assertThat(resultPatient.getFirstName()).isEqualTo(createPatientCommand.getFirstName());
        Assertions.assertThat(resultPatient.getLastName()).isEqualTo(createPatientCommand.getLastName());
        Assertions.assertThat(resultPatient.getAnimalName()).isEqualTo(createPatientCommand.getAnimalName());
        Assertions.assertThat(resultPatient.getAnimalSpecies()).isEqualTo(createPatientCommand.getAnimalSpecies());
        Assertions.assertThat(resultPatient.getEmail()).isEqualTo(createPatientCommand.getEmail());
        Assertions.assertThat(resultPatient.getRace()).isEqualTo(createPatientCommand.getRace());

    }

    @Test
    void shouldGetById() throws Exception {
        //given
        Patient patient = new Patient(
                "Jack",
                "Dog",
                "buldog",
                12L,
                "Jan",
                "Kowalski",
                "gofecki@gmail.com"
        );
        PatientView expectedResult = PatientView.buildPatientView(patient);
        when(patientRepository.findById(any())).thenReturn(Optional.of(patient));

        //when
        MvcResult mvcResult = mockMvc.perform(get(GET_BY_ID_PATIENT_ENDPOINT)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();
        //then
        String contentAsString = mvcResult.getResponse().getContentAsString();
        PatientView result = mapper.readValue(contentAsString, PatientView.class);

        Assertions.assertThat(result).isEqualTo(expectedResult);
    }
    @Test
    public void shouldGetPatientsInList() throws Exception {
        //given
        Patient patient1 = new Patient(
                "Jack",
                "Dog",
                "buldog",
                12L,
                "Jan",
                "Kowalski",
                "gofecki@gmail.com"
        );
        Patient patient2 = new Patient(
                "Joe",
                "cat",
                "syiamiese",
                15L,
                "Anna",
                "Nowak",
                "annaNowak@gmail.com"
        );
        List<Patient> patientList = List.of(patient1,patient2);
        List<PatientListView> expectedResult = List.of(PatientListView.fromPatient(patient1),
                PatientListView.fromPatient(patient2));
        Page<Patient> patientPage = new PageImpl<>(patientList);
        when(patientRepository.findAll(any(PageRequest.class))).thenReturn(patientPage);

        //when
        MvcResult mvcResult = mockMvc.perform(get(GET_PATIENTS_IN_LIST)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();
        //then
        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<PatientListView> resultList= mapper.readValue(contentAsString,
                new TypeReference<List<PatientListView>>() {
                });

        Assertions.assertThat(resultList).isNotEmpty();
        Assertions.assertThat(expectedResult).isEqualTo(resultList);
    }
}