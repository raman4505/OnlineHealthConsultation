package model;

public class Patient extends User {

    private int patientId;
    private String dateOfBirth;
    private String gender;
    private String address;

    public Patient() {
        super();
    }

    public Patient(int userId, String fullName, String email,
                   String password, String phone,
                   int patientId, String dateOfBirth,
                   String gender, String address) {

        super(userId, fullName, email, password, phone);

        this.patientId = patientId;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.address = address;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public void displayInfo() {
        System.out.println("Patient: " + getFullName());
        System.out.println("Email: " + getEmail());
        System.out.println("Gender: " + gender);
        System.out.println("Address: " + address);
    }
}