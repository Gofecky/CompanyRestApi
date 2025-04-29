package pl.kurs.companyrestapi.controllers;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.kurs.companyrestapi.BaseTest;
import pl.kurs.companyrestapi.commands.CreateAppointmentCommand;
import pl.kurs.companyrestapi.commands.SearchAvailableAppointmentsCommand;
import pl.kurs.companyrestapi.models.Appointment;
import pl.kurs.companyrestapi.models.Doctor;
import pl.kurs.companyrestapi.models.Patient;
import pl.kurs.companyrestapi.repositories.AppointmentRepository;
import pl.kurs.companyrestapi.repositories.DoctorRepository;
import pl.kurs.companyrestapi.repositories.PatientRepository;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AppointmentControllerTest extends BaseTest {

    private static final String CREATE_APPOINTMENT_ENDPOINT = "/appointment/create";
    private static final String CONFIRM_TOKEN = "/appointment/confirm/token";
    private static final String DELETE_TOKEN = "/appointment/cancel/token";
    private static final String CHECK_AVALIBILITY = "/appointment/check";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Captor
    private ArgumentCaptor<Appointment> argumentCaptor;
    @MockitoBean
    private AppointmentRepository appointmentRepository;
    @MockitoBean
    DoctorRepository doctorRepository;
    @MockitoBean
    PatientRepository patientRepository;
    @BeforeEach
    void setup() {
        when(appointmentRepository.deleteAllByConfirmedFalseAndCreatedAtBefore(any()))
                .thenReturn(0);
    }

    @Test
    public void shouldCreateAppointment() throws Exception {
        //given
        Doctor doctor = new Doctor(
                "Jan",
                "Kowalski",
                "chirurgy",
                "Dogs",
                BigDecimal.valueOf(90),
                "1234567890"
        );
        Patient patient = new Patient(
                "Jack",
                "Dog",
                "buldog",
                12L,
                "Jan",
                "Kowalski",
                "gofecki@gmail.com"
        );
        CreateAppointmentCommand createAppointmentCommand = CreateAppointmentCommand
                .builder()
                .doctorId(0L)
                .patientId(0L)
                        .date(LocalDateTime.of(2024,10,10,14,10))
                                .build();

        when(doctorRepository.findById(any())).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(any())).thenReturn(Optional.of(patient));
        when(appointmentRepository.save(argumentCaptor.capture())).thenReturn(null);

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(CREATE_APPOINTMENT_ENDPOINT)
                .content(mapper.writeValueAsString(createAppointmentCommand))
                .contentType("application/json"))
                .andExpect(status().isCreated())
                .andReturn();

        //then
        Assertions.assertThat(mvcResult.getResponse().getContentAsString()).isNotEmpty();

        Appointment resultAppointment = argumentCaptor.getValue();

        Assertions.assertThat(resultAppointment.getDate()).isEqualTo(createAppointmentCommand.getDate());
        Assertions.assertThat(resultAppointment.getDoctor()).isEqualTo(doctor);
        Assertions.assertThat(resultAppointment.getPatient()).isEqualTo(patient);

    }
    @Test
    void shouldConfirmAppointment() throws Exception {
        //given
        Appointment appointment = new Appointment();
        String token = "token";
        appointment.setToken(token);
        appointment.setCreatedAt(LocalDateTime.now());
        when(appointmentRepository.findByToken(any())).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(argumentCaptor.capture())).thenReturn(null);

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(CONFIRM_TOKEN)
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        //then
        Appointment resultAppointment = argumentCaptor.getValue();
        Assertions.assertThat(resultAppointment.isConfirmed()).isTrue();
    }

    @Test
    void shouldDeleteAppointment() throws Exception {
        //given
        Appointment appointment = new Appointment();
        String token = "token";
        appointment.setToken(token);
        appointment.setCreatedAt(LocalDateTime.now());
        when(appointmentRepository.findByToken(any())).thenReturn(Optional.of(appointment));

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(DELETE_TOKEN)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        //then
        verify(appointmentRepository).delete(argumentCaptor.capture());
        Appointment deletedAppointment = argumentCaptor.getValue();

        assertEquals(token, deletedAppointment.getToken());
    }
    @Test
    void shouldReturnAvailableAppointmentSlots() throws Exception {
        // given
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setFirstName("Jan");
        doctor.setLastName("Kowalski");
        doctor.setFired(false);

        String type = "chirurgia";
        String animal = "pies";
        LocalDateTime from = LocalDateTime.of(2025, 4, 25, 10, 0);
        LocalDateTime to = LocalDateTime.of(2025, 4, 25, 12, 0);

        SearchAvailableAppointmentsCommand cmd = new SearchAvailableAppointmentsCommand();
        cmd.setType(type);
        cmd.setAnimal(animal);
        cmd.setFrom(from);
        cmd.setTo(to);

        when(doctorRepository.findByMedicalSpecializationIgnoreCaseAndAnimalSpecializationIgnoreCaseAndIsFiredFalse(type, animal))
                .thenReturn(List.of(doctor));

        // symulujemy, że nie ma zajętości
        when(appointmentRepository.existsByDoctorAndDate(any(), any()))
                .thenReturn(false);

        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(CHECK_AVALIBILITY)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(cmd)))
                .andExpect(status().isOk())
                .andReturn();

        // then
        String response = result.getResponse().getContentAsString();
        // możesz też zamienić to w obiekt listy JSON, jeśli chcesz dokładniej
        System.out.println("Response: " + response);

        // dodatkowe verify
        verify(doctorRepository).findByMedicalSpecializationIgnoreCaseAndAnimalSpecializationIgnoreCaseAndIsFiredFalse(type, animal);
        verify(appointmentRepository, atLeast(1)).existsByDoctorAndDate(eq(doctor), any());
    }

    // narzędzie do konwersji obiektu na JSON
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}