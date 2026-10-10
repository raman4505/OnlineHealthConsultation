package gui;

import model.User;
import util.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDashboard extends JFrame {

    private final User user;

    private JButton usersButton;
    private JButton appointmentsButton;
    private JButton logoutButton;

    public AdminDashboard(User user) {
        this.user = user;

        setTitle("Admin Dashboard");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(15, 20));
        mainPanel.setBorder(
            BorderFactory.createEmptyBorder(25, 40, 30, 40)
        );

        // Welcome heading
        JLabel welcomeLabel = new JLabel(
            "Welcome, " + user.getFullName(),
            SwingConstants.CENTER
        );
        welcomeLabel.setFont(
            new Font("Arial", Font.BOLD, 26)
        );

        // Buttons
        usersButton = new JButton("View All Users");
        appointmentsButton = new JButton("View All Appointments");
        logoutButton = new JButton("Logout");

        Font buttonFont = new Font("Arial", Font.BOLD, 16);

        usersButton.setFont(buttonFont);
        appointmentsButton.setFont(buttonFont);
        logoutButton.setFont(buttonFont);

        // Button layout
        JPanel buttonPanel = new JPanel(
            new GridLayout(3, 1, 15, 15)
        );

        buttonPanel.add(usersButton);
        buttonPanel.add(appointmentsButton);
        buttonPanel.add(logoutButton);

        mainPanel.add(welcomeLabel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        add(mainPanel);

        // Button actions
        usersButton.addActionListener(e -> viewAllUsers());

        appointmentsButton.addActionListener(
            e -> viewAllAppointments()
        );

        logoutButton.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });

        setVisible(true);
    }

    // Display all registered users
    private void viewAllUsers() {

        String sql =
            "SELECT user_id, full_name, email, role, phone " +
            "FROM users ORDER BY user_id";

        String[] columns = {
            "User ID", "Full Name", "Email", "Role", "Phone"
        };

        DefaultTableModel tableModel =
            new DefaultTableModel(columns, 0) {
                @Override
                public boolean isCellEditable(
                    int row, int column
                ) {
                    return false;
                }
            };

        try (
            Connection con = DBConnection.getConnection()
        ) {
            if (con == null) {
                throw new SQLException(
                    "Could not connect to the database."
                );
            }

            try (
                PreparedStatement ps =
                    con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
            ) {
                while (rs.next()) {
                    tableModel.addRow(new Object[] {
                        rs.getInt("user_id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("role"),
                        rs.getString("phone")
                    });
                }
            }

            JTable table = new JTable(tableModel);
            table.setAutoCreateRowSorter(true);
            table.setFillsViewportHeight(true);

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(
                new Dimension(750, 350)
            );

            JOptionPane.showMessageDialog(
                this,
                scrollPane,
                "All Registered Users",
                JOptionPane.INFORMATION_MESSAGE
            );

        } catch (SQLException ex) {
            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                this,
                "Could not load users.\n" + ex.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Display all appointments
    private void viewAllAppointments() {

        String sql =
            "SELECT a.appointment_id, " +
            "p.user_id AS patient_user_id, " +
            "pu.full_name AS patient_name, " +
            "hp.user_id AS professional_user_id, " +
            "hu.full_name AS professional_name, " +
            "a.appointment_date, " +
            "a.appointment_time, " +
            "a.status, " +
            "a.consultation_notes " +
            "FROM appointments a " +
            "JOIN patients p ON a.patient_id = p.patient_id " +
            "JOIN users pu ON p.user_id = pu.user_id " +
            "JOIN healthcare_professionals hp " +
            "ON a.professional_id = hp.professional_id " +
            "JOIN users hu ON hp.user_id = hu.user_id " +
            "ORDER BY a.appointment_date, a.appointment_time";

        String[] columns = {
            "Appointment ID",
            "Patient ID",
            "Patient Name",
            "Professional ID",
            "Professional Name",
            "Date",
            "Time",
            "Status",
            "Consultation Notes"
        };

        DefaultTableModel tableModel =
            new DefaultTableModel(columns, 0) {
                @Override
                public boolean isCellEditable(
                    int row, int column
                ) {
                    return false;
                }
            };

        try (
            Connection con = DBConnection.getConnection()
        ) {
            if (con == null) {
                throw new SQLException(
                    "Could not connect to the database."
                );
            }

            try (
                PreparedStatement ps =
                    con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
            ) {
                while (rs.next()) {
                    tableModel.addRow(new Object[] {
                        rs.getInt("appointment_id"),
                        rs.getInt("patient_user_id"),
                        rs.getString("patient_name"),
                        rs.getInt("professional_user_id"),
                        rs.getString("professional_name"),
                        rs.getDate("appointment_date"),
                        rs.getTime("appointment_time"),
                        rs.getString("status"),
                        rs.getString("consultation_notes")
                    });
                }
            }

            JTable table = new JTable(tableModel);
            table.setAutoCreateRowSorter(true);
            table.setFillsViewportHeight(true);

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(
                new Dimension(1000, 350)
            );

            JOptionPane.showMessageDialog(
                this,
                scrollPane,
                "All Appointments",
                JOptionPane.INFORMATION_MESSAGE
            );

        } catch (SQLException ex) {
            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                this,
                "Could not load appointments.\n"
                    + ex.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}