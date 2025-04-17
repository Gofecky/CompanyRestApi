package pl.kurs.companyrestapi.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendAppointmentToken(String to, String token) {
        String confirmUrl = "http://localhost:8080/appointment/confirm/" + token;

        String subject = "Potwierdź wizytę w VetClinic";
        String message = String.format(
                "Cześć!\n\nAby potwierdzić swoją wizytę, kliknij w poniższy link:\n%s\n\nPozdrawiamy,\nZespół VetClinic",
                confirmUrl
        );

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setSubject(subject);
        email.setText(message);

        mailSender.send(email);
    }
}