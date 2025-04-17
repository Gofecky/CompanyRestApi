package pl.kurs.companyrestapi.views;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AppointmentSlotView {

    private Long doctorId;
    private String doctorName;
    private LocalDateTime availableDate;

    public AppointmentSlotView(Long doctorId, String doctorName, LocalDateTime availableDate) {
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.availableDate = availableDate;
    }

}