package pl.kurs.companyrestapi.commands;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SearchAvailableAppointmentsCommand {

    @NotBlank
    private String type;

    @NotBlank
    private String animal;

    @NotNull
    private LocalDateTime from;

    @NotNull
    private LocalDateTime to;


}