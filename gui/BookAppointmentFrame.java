package gui;

import dao.AppointmentDAO;
import model.Appointment;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BookAppointmentFrame extends JFrame {

    private User user;

    private JTextField dateField;
    private JTextField timeField;
    private JTextArea notesArea;

    public BookAppointmentFrame(User user) {

        this.user = user;

        setTitle("Book Appointment");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel(
                "Book Healthcare Appointment",
                SwingConstants.CENTER
        );

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 20)
        );

        JPanel formPanel = new JPanel(
                new GridLayout(4, 2, 10, 10)
        );

        formPanel.setBorder(
                BorderFactory.createEmptyBorder(30, 30, 20, 30)
        );

        JLabel dateLabel = new JLabel(
                "Date (YYYY-MM-DD):"
        );

        JLabel timeLabel = new JLabel(
                "Time (HH:MM):"
        );

        JLabel notesLabel = new JLabel(
                "Notes:"
        );

        dateField = new JTextField();
        timeField = new JTextField();

        notesArea = new JTextArea(4, 20);

        formPanel.add(dateLabel);
        formPanel.add(dateField);

        formPanel.add(timeLabel);
        formPanel.add(timeField);

        formPanel.add(notesLabel);
        formPanel.add(new JScrollPane(notesArea));

        JButton bookButton =
                new JButton("Book Appointment");

        JButton cancelButton =
                new JButton("Cancel");

        formPanel.add(bookButton);
        formPanel.add(cancelButton);

        add(titleLabel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);

        bookButton.addActionListener(e -> bookAppointment());

        cancelButton.addActionListener(e -> dispose());

        setVisible(true);
    }


    private void bookAppointment() {

        try {

            String date = dateField.getText().trim();
            String time = timeField.getText().trim();
            String notes = notesArea.getText().trim();

            if (date.isEmpty() || time.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter date and time."
                );

                return;
            }

            LocalDateTime appointmentTime =
                    LocalDateTime.parse(
                            date + "T" + time
                    );

            AppointmentDAO appointmentDAO =
                    new AppointmentDAO();

            int patientId =
                    appointmentDAO.getPatientIdByUserId(
                            user.getUserId()
                    );

            if (patientId == -1) {

                JOptionPane.showMessageDialog(
                        this,
                        "Patient record not found."
                );

                return;
            }

            // Currently using the professional created in MySQL
            int professionalId = 1;

            Appointment appointment =
                    new Appointment(
                            0,
                            patientId,
                            professionalId,
                            appointmentTime,
                            "BOOKED",
                            notes
                    );

            boolean success =
                    appointmentDAO.bookAppointment(
                            appointment
                    );

            if (success) {

                JOptionPane.showMessageDialog(
                        this,
                        "Appointment booked successfully!"
                );

                dispose();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Unable to book appointment.",
                        "Booking Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid date or time.\n\n"
                    + "Use:\n"
                    + "Date: YYYY-MM-DD\n"
                    + "Time: HH:MM",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}