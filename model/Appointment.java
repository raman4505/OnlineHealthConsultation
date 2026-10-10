package model;

import java.time.LocalDateTime;

public class Appointment {

    private int appointmentId;
    private int patientId;
    private String patientName;
    private int professionalId;
    private LocalDateTime appointmentTime;
    private String status;
    private String consultationNotes;

    // Default constructor
    public Appointment() {
    }

    // Parameterized constructor
    public Appointment(int appointmentId, int patientId, int professionalId,
                       LocalDateTime appointmentTime, String status,
                       String consultationNotes) {

        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.professionalId = professionalId;
        this.appointmentTime = appointmentTime;
        this.status = status;
        this.consultationNotes = consultationNotes;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getProfessionalId() {
        return professionalId;
    }

    public void setProfessionalId(int professionalId) {
        this.professionalId = professionalId;
    }

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalDateTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getConsultationNotes() {
        return consultationNotes;
    }

    public void setConsultationNotes(String consultationNotes) {
        this.consultationNotes = consultationNotes;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentId=" + appointmentId +
                ", patientId=" + patientId +
                ", professionalId=" + professionalId +
                ", appointmentTime=" + appointmentTime +
                ", status='" + status + '\'' +
                ", consultationNotes='" + consultationNotes + '\'' +
                '}';
    }
    public String getPatientName() {
    return patientName;
}

public void setPatientName(String patientName) {
    this.patientName = patientName;
}
}