package pl.kurs.companyrestapi.commands;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAppointmentCommand {

    public Long doctorId;
    public Long patientId;
    public LocalDateTime date;
}