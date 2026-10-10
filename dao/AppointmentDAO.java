package dao;

import model.Appointment;
import util.DBConnection;

import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class AppointmentDAO {

    // Get patient_id using the logged-in user's user_id
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


    // Book a new appointment
    public boolean bookAppointment(Appointment appointment) {

        String sql = """
                INSERT INTO appointments
                (patient_id, professional_id, appointment_date,
                 appointment_time, status, consultation_notes)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, appointment.getPatientId());
            ps.setInt(2, appointment.getProfessionalId());

            LocalDateTime dateTime = appointment.getAppointmentTime();

            ps.setDate(
                    3,
                    Date.valueOf(dateTime.toLocalDate())
            );

            ps.setTime(
                    4,
                    Time.valueOf(dateTime.toLocalTime())
            );

            ps.setString(5, appointment.getStatus());
            ps.setString(6, appointment.getConsultationNotes());

            int rows = ps.executeUpdate();

            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    // Get all appointments for a patient
    public List<Appointment> getAppointmentsByPatientId(int patientId) {

        List<Appointment> appointments = new ArrayList<>();

        String sql = """
                SELECT *
                FROM appointments
                WHERE patient_id = ?
                ORDER BY appointment_date, appointment_time
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, patientId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                LocalDateTime appointmentDateTime =
                        LocalDateTime.of(
                                rs.getDate("appointment_date").toLocalDate(),
                                rs.getTime("appointment_time").toLocalTime()
                        );

                Appointment appointment = new Appointment(
                        rs.getInt("appointment_id"),
                        rs.getInt("patient_id"),
                        rs.getInt("professional_id"),
                        appointmentDateTime,
                        rs.getString("status"),
                        rs.getString("consultation_notes")
                );

                appointments.add(appointment);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return appointments;
    }
    
    // Get all appointments assigned to a healthcare professional
    public List<Appointment> getAppointmentsByProfessionalId(int professionalId) {

        List<Appointment> appointments = new ArrayList<>();

        String sql = """
            SELECT a.appointment_id,
                   a.patient_id,
                   a.professional_id,
                   a.appointment_date,
                   a.appointment_time,
                   a.status,
                   a.consultation_notes,
                   u.full_name AS patient_name
            FROM appointments a
            JOIN patients p ON a.patient_id = p.patient_id
            JOIN users u ON p.user_id = u.user_id
            WHERE a.professional_id = ?
            ORDER BY a.appointment_date, a.appointment_time
            """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, professionalId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LocalDateTime dateTime = LocalDateTime.of(
                        rs.getDate("appointment_date").toLocalDate(),
                        rs.getTime("appointment_time").toLocalTime()
                    );

                    Appointment appointment = new Appointment(
                        rs.getInt("appointment_id"),
                        rs.getInt("patient_id"),
                        rs.getInt("professional_id"),
                        dateTime,
                        rs.getString("status"),
                        rs.getString("consultation_notes")
                    );

                    appointment.setPatientName(rs.getString("patient_name"));
                    appointments.add(appointment);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return appointments;
    }
    public boolean updateConsultationNotes(int appointmentId, String notes) {
    String sql = "UPDATE appointments SET consultation_notes = ? WHERE appointment_id = ?";

    try (
        java.sql.Connection con = util.DBConnection.getConnection();
        java.sql.PreparedStatement ps = con.prepareStatement(sql)
    ) {
        ps.setString(1, notes);
        ps.setInt(2, appointmentId);

        return ps.executeUpdate() > 0;

    } catch (java.sql.SQLException e) {
        e.printStackTrace();
        return false;
    }
}
}