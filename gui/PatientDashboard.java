package gui;

import model.User;

import javax.swing.*;
import java.awt.*;

public class PatientDashboard extends JFrame {

    private User user;

    public PatientDashboard(User user) {

        this.user = user;

        setTitle("Patient Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel welcomeLabel = new JLabel(
                "Welcome, " + user.getFullName(),
                SwingConstants.CENTER
        );

        welcomeLabel.setFont(
                new Font("Arial", Font.BOLD, 22)
        );

        JButton bookButton =
                new JButton("Book Appointment");

        JButton appointmentsButton =
                new JButton("View Appointments");

        JButton healthRecordButton =
                new JButton("Health Records");

        JButton logoutButton =
                new JButton("Logout");

        JPanel buttonPanel =
                new JPanel(new GridLayout(4, 1, 10, 10));

        buttonPanel.setBorder(
                BorderFactory.createEmptyBorder(30, 100, 30, 100)
        );

        buttonPanel.add(bookButton);
        buttonPanel.add(appointmentsButton);
        buttonPanel.add(healthRecordButton);
        buttonPanel.add(logoutButton);

        add(welcomeLabel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

       bookButton.addActionListener(e -> {

    new BookAppointmentFrame(user);

});

        appointmentsButton.addActionListener(e -> {

    new AppointmentsFrame(user);

});

       healthRecordButton.addActionListener(e -> {

    new HealthRecordsFrame(user);

});
        logoutButton.addActionListener(e -> {

            new LoginFrame();
            dispose();

        });

        setVisible(true);
    }
}