package pl.kurs.companyrestapi.commands;

import pl.kurs.companyrestapi.models.Doctor;

public class FireDoctorCommand {

    private double isFired;

    public void fireDoctor(Doctor doctor) {
        doctor.setFired(true);
    }
}