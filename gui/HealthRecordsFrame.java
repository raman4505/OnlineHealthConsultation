package gui;

import dao.HealthRecordDAO;
import model.HealthRecord;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class HealthRecordsFrame extends JFrame {

    private User user;
    private JTable recordTable;
    private DefaultTableModel tableModel;

    public HealthRecordsFrame(User user) {

        this.user = user;

        setTitle("Health Records");
        setSize(850, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel(
                "My Health Records",
                SwingConstants.CENTER
        );

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 22)
        );

        String[] columns = {
                "Record ID",
                "Date",
                "Diagnosis",
                "Prescription",
                "Notes"
        };

        tableModel = new DefaultTableModel(columns, 0);

        recordTable = new JTable(tableModel);

        JScrollPane scrollPane =
                new JScrollPane(recordTable);

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

        refreshButton.addActionListener(e -> loadHealthRecords());

        closeButton.addActionListener(e -> dispose());

        loadHealthRecords();

        setVisible(true);
    }

    private void loadHealthRecords() {

        tableModel.setRowCount(0);

        HealthRecordDAO healthRecordDAO =
                new HealthRecordDAO();

        int patientId =
                healthRecordDAO.getPatientIdByUserId(
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

        List<HealthRecord> records =
                healthRecordDAO.getHealthRecordsByPatientId(
                        patientId
                );

        for (HealthRecord record : records) {

            Object[] row = {
                    record.getRecordId(),
                    record.getRecordDate(),
                    record.getDiagnosis(),
                    record.getPrescription(),
                    record.getNotes()
            };

            tableModel.addRow(row);
        }

        if (records.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "No health records found."
            );
        }
    }
}