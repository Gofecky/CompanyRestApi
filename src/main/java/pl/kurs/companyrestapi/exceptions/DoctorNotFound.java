package pl.kurs.companyrestapi.exceptions;

public class DoctorNotFound extends Throwable {
    public DoctorNotFound() {
        super("Cannot find doctor");
    }
}