package pl.kurs.companyrestapi.exceptions;

public class PatientNotFound extends Throwable {
    public PatientNotFound() {
        super("Patients id not found");
    }
}