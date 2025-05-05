package pl.kurs.companyrestapi.gui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;

public class DoctorRegistrationGUI extends JFrame {
    private JTextField nameField;
    private JTextField surnameField;
    private JTextField specializationField;

    public DoctorRegistrationGUI() {
        setTitle("Rejestracja Lekarza");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2));

        add(new JLabel("Imię:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Nazwisko:"));
        surnameField = new JTextField();
        add(surnameField);

        add(new JLabel("Specjalizacja:"));
        specializationField = new JTextField();
        add(specializationField);

        JButton submitButton = new JButton("Zarejestruj");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerDoctor();
            }
        });
        add(submitButton);

        setVisible(true);
    }

    private void registerDoctor() {
        try {
            URL url = new URL("http://localhost:8080/doctor");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = String.format(
                    "{\"name\": \"%s\", \"surname\": \"%s\", \"type\": \"%s\"}",
                    nameField.getText(),
                    surnameField.getText(),
                    specializationField.getText()
            );

            try(OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int code = conn.getResponseCode();
            if (code == 201) {
                JOptionPane.showMessageDialog(this, "Lekarz zarejestrowany pomyślnie!");
            } else {
                JOptionPane.showMessageDialog(this, "Błąd podczas rejestracji lekarza.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Wystąpił błąd: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new DoctorRegistrationGUI();
    }
}
