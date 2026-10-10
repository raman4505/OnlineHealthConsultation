package gui;

import dao.AppointmentDAO;
import model.Appointment;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AppointmentsFrame extends JFrame {

    private User user;
    private JTable appointmentTable;
    private DefaultTableModel tableModel;

    public AppointmentsFrame(User user) {

        this.user = user;

        setTitle("My Appointments");
        setSize(750, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel(
                "My Appointments",
                SwingConstants.CENTER
        );

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 22)
        );

        String[] columns = {
                "Appointment ID",
                "Professional ID",
                "Date",
                "Time",
                "Status",
                "Notes"
        };

        tableModel = new DefaultTableModel(columns, 0);

        appointmentTable = new JTable(tableModel);

        JScrollPane scrollPane =
                new JScrollPane(appointmentTable);

        JButton refreshButton =
                new JButton("Refresh");

        JButton closeButton =
                new JButton("Close");

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(refreshButton);
        buttonPanel.add(closeButton);

        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshButton.addActionListener(e -> loadAppointments());

        closeButton.addActionListener(e -> dispose());

        loadAppointments();

        setVisible(true);
    }


    private void loadAppointments() {

        tableModel.setRowCount(0);

        AppointmentDAO appointmentDAO =
                new AppointmentDAO();

        int patientId =
                appointmentDAO.getPatientIdByUserId(
                        user.getUserId()
                );

        if (patientId == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Patient record not found.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        List<Appointment> appointments =
                appointmentDAO.getAppointmentsByPatientId(
                        patientId
                );

        for (Appointment appointment : appointments) {

            String date =
                    appointment.getAppointmentTime()
                            .toLocalDate()
                            .toString();

            String time =
        appointment.getAppointmentTime()
                .toLocalTime()
                .toString();

Object[] row = {
        appointment.getAppointmentId(),
        appointment.getProfessionalId(),
        date,
        time,
        appointment.getStatus(),
        appointment.getConsultationNotes()
};

tableModel.addRow(row);
}

if (appointments.isEmpty()) {

    JOptionPane.showMessageDialog(
            this,
            "No appointments found."
    );
}
}
}