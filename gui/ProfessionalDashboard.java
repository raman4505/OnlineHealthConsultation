package gui;

import model.User;
import model.Appointment;
import util.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ProfessionalDashboard extends JFrame {

    private final User user;

    public ProfessionalDashboard(User user) {
        this.user = user;

        setTitle("Healthcare Professional Dashboard");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        String name = user.getFullName();
        if (name != null && name.startsWith("Dr. ")) {
            name = name.substring(4);
        }

        JLabel welcomeLabel = new JLabel(
                "Welcome, Dr. " + name,
                SwingConstants.CENTER
        );
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setBorder(
                BorderFactory.createEmptyBorder(20, 10, 10, 10)
        );

        JButton appointmentsButton = new JButton("View Appointments");
        JButton patientsButton = new JButton("Patient Records");
        JButton consultationButton = new JButton("Consultations");
        JButton logoutButton = new JButton("Logout");

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 12, 12));
        buttonPanel.setBorder(
                BorderFactory.createEmptyBorder(20, 100, 30, 100)
        );

        buttonPanel.add(appointmentsButton);
        buttonPanel.add(patientsButton);
        buttonPanel.add(consultationButton);
        buttonPanel.add(logoutButton);

        add(welcomeLabel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        appointmentsButton.addActionListener(
                e -> viewAppointments()
        );

        patientsButton.addActionListener(
                e -> viewPatientRecords()
        );

        consultationButton.addActionListener(
                e -> manageConsultation()
        );

        logoutButton.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });

        setVisible(true);
    }

    private int getProfessionalId(int userId) {
        String sql =
                "SELECT professional_id FROM healthcare_professionals "
                + "WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                return -1;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, userId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("professional_id");
                    }
                }
            }
        } catch (SQLException ex) {
            showError("Could not find the professional profile.", ex);
        }

        return -1;
    }

    private void viewAppointments() {
        int professionalId = getProfessionalId(user.getUserId());

        if (professionalId == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "No healthcare professional profile was found "
                    + "for this account."
            );
            return;
        }

        String sql =
                "SELECT appointment_id, patient_id, appointment_date, "
                + "appointment_time, status, consultation_notes "
                + "FROM appointments WHERE professional_id = ? "
                + "ORDER BY appointment_date, appointment_time";

        DefaultTableModel tableModel = new DefaultTableModel(
                new String[]{
                    "Appointment ID",
                    "Patient ID",
                    "Date & Time",
                    "Status",
                    "Consultation Notes"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                return;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, professionalId);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String dateTime =
                                rs.getDate("appointment_date") + " "
                                + rs.getTime("appointment_time");

                        tableModel.addRow(new Object[]{
                            rs.getInt("appointment_id"),
                            rs.getInt("patient_id"),
                            dateTime,
                            rs.getString("status"),
                            rs.getString("consultation_notes")
                        });
                    }
                }
            }
        } catch (SQLException ex) {
            showError("Could not load appointments.", ex);
            return;
        }

        JTable table = new JTable(tableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(650, 300));

        JOptionPane.showMessageDialog(
                this,
                scrollPane,
                "Professional Appointments",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void viewPatientRecords() {
        int professionalId = getProfessionalId(user.getUserId());

        if (professionalId == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "No healthcare professional profile was found."
            );
            return;
        }

        String sql =
                "SELECT DISTINCT u.user_id, u.full_name, u.email, "
                + "u.phone, p.patient_id "
                + "FROM appointments a "
                + "JOIN patients p ON a.patient_id = p.patient_id "
                + "JOIN users u ON p.user_id = u.user_id "
                + "WHERE a.professional_id = ? "
                + "ORDER BY u.full_name";

        DefaultTableModel tableModel = new DefaultTableModel(
                new String[]{
                    "Patient ID", "Name", "Email", "Phone"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                return;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, professionalId);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        tableModel.addRow(new Object[]{
                            rs.getInt("patient_id"),
                            rs.getString("full_name"),
                            rs.getString("email"),
                            rs.getString("phone")
                        });
                    }
                }
            }
        } catch (SQLException ex) {
            showError("Could not load patient records.", ex);
            return;
        }

        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "No patients with appointments were found."
            );
            return;
        }

        JTable table = new JTable(tableModel);

        JOptionPane.showMessageDialog(
                this,
                new JScrollPane(table),
                "Patients With Appointments",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void manageConsultation() {
        int professionalId = getProfessionalId(user.getUserId());

        if (professionalId == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "No healthcare professional profile was found."
            );
            return;
        }

        String selectSql =
                "SELECT appointment_id, patient_id, appointment_date, "
                + "appointment_time, status "
                + "FROM appointments WHERE professional_id = ? "
                + "AND status = 'BOOKED' "
                + "ORDER BY appointment_date, appointment_time";

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                return;
            }

            int[] appointmentIds;
            String[] choices;

            try (PreparedStatement ps = con.prepareStatement(selectSql)) {
                ps.setInt(1, professionalId);

                try (ResultSet rs = ps.executeQuery()) {
                    java.util.ArrayList<Integer> ids =
                            new java.util.ArrayList<>();
                    java.util.ArrayList<String> labels =
                            new java.util.ArrayList<>();

                    while (rs.next()) {
                        ids.add(rs.getInt("appointment_id"));
                        labels.add(
                                "Appointment ID: "
                                + rs.getInt("appointment_id")
                                + " | Patient ID: "
                                + rs.getInt("patient_id")
                                + " | "
                                + rs.getDate("appointment_date")
                                + " "
                                + rs.getTime("appointment_time")
                        );
                    }

                    appointmentIds = new int[ids.size()];
                    choices = new String[labels.size()];

                    for (int i = 0; i < ids.size(); i++) {
                        appointmentIds[i] = ids.get(i);
                        choices[i] = labels.get(i);
                    }
                }
            }

            if (choices.length == 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "No booked appointments are available."
                );
                return;
            }

            String selected = (String) JOptionPane.showInputDialog(
                    this,
                    "Select an appointment:",
                    "Consultation Management",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    choices,
                    choices[0]
            );

            if (selected == null) {
                return;
            }

            int selectedIndex = java.util.Arrays.asList(choices)
                    .indexOf(selected);
            int appointmentId = appointmentIds[selectedIndex];

            String notes = JOptionPane.showInputDialog(
                    this,
                    "Enter consultation notes:",
                    "Consultation Notes",
                    JOptionPane.PLAIN_MESSAGE
            );

            if (notes == null) {
                return;
            }

            String updateSql =
                    "UPDATE appointments SET consultation_notes = ?, "
                    + "status = 'COMPLETED' "
                    + "WHERE appointment_id = ? AND professional_id = ?";

            try (PreparedStatement update =
                    con.prepareStatement(updateSql)) {
                update.setString(1, notes.trim());
                update.setInt(2, appointmentId);
                update.setInt(3, professionalId);

                int rowsUpdated = update.executeUpdate();

                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Consultation notes saved successfully."
                    );
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "No appointment was updated."
                    );
                }
            }

        } catch (SQLException ex) {
            showError("Could not save consultation notes.", ex);
        }
    }

    private void showError(String message, Exception ex) {
        ex.printStackTrace();

        JOptionPane.showMessageDialog(
                this,
                message + "\n" + ex.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}