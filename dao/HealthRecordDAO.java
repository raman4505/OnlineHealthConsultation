package dao;

import model.HealthRecord;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HealthRecordDAO {

    // Add a health record
    public boolean addHealthRecord(HealthRecord record) {

        String sql = "INSERT INTO health_records " +
                "(patient_id, record_date, diagnosis, prescription, notes) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, record.getPatientId());
            ps.setDate(2, java.sql.Date.valueOf(record.getRecordDate()));
            ps.setString(3, record.getDiagnosis());
            ps.setString(4, record.getPrescription());
            ps.setString(5, record.getNotes());

            int rows = ps.executeUpdate();

            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get health records for a patient
    public List<HealthRecord> getHealthRecordsByPatientId(int patientId) {

        List<HealthRecord> records = new ArrayList<>();

        String sql = "SELECT * FROM health_records " +
                "WHERE patient_id = ? " +
                "ORDER BY record_date DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, patientId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                HealthRecord record = new HealthRecord(
                        rs.getInt("record_id"),
                        rs.getInt("patient_id"),
                        rs.getDate("record_date").toLocalDate(),
                        rs.getString("diagnosis"),
                        rs.getString("prescription"),
                        rs.getString("notes")
                );

                records.add(record);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return records;
    }
    // Get patient ID using the logged-in user's ID
public int getPatientIdByUserId(int userId) {

    String sql = "SELECT patient_id FROM patients WHERE user_id = ?";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, userId);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt("patient_id");
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return -1;
}
}