package model;

import java.time.LocalDate;

public class HealthRecord {

    private int recordId;
    private int patientId;
    private LocalDate recordDate;
    private String diagnosis;
    private String prescription;
    private String notes;

    // Default constructor
    public HealthRecord() {
    }

    // Parameterized constructor
    public HealthRecord(int recordId, int patientId,
                        LocalDate recordDate,
                        String diagnosis,
                        String prescription,
                        String notes) {

        this.recordId = recordId;
        this.patientId = patientId;
        this.recordDate = recordDate;
        this.diagnosis = diagnosis;
        this.prescription = prescription;
        this.notes = notes;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}