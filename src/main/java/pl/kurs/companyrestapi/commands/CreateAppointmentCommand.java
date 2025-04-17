package pl.kurs.companyrestapi.commands;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateAppointmentCommand {

    public Long doctorId;
    public Long patientId;
    public LocalDateTime date;
}