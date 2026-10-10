package model;

public class HealthcareProfessional extends User {

    private int professionalId;
    private String specialization;
    private String licenseNumber;
    private int experienceYears;

    public HealthcareProfessional() {
        super();
    }

    public HealthcareProfessional(
            int userId,
            String fullName,
            String email,
            String password,
            String phone,
            int professionalId,
            String specialization,
            String licenseNumber,
            int experienceYears) {

        super(userId, fullName, email, password, phone);

        this.professionalId = professionalId;
        this.specialization = specialization;
        this.licenseNumber = licenseNumber;
        this.experienceYears = experienceYears;
    }

    public int getProfessionalId() {
        return professionalId;
    }

    public void setProfessionalId(int professionalId) {
        this.professionalId = professionalId;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }

    @Override
    public void displayInfo() {
        System.out.println("Healthcare Professional: " + getFullName());
        System.out.println("Email: " + getEmail());
        System.out.println("Specialization: " + specialization);
        System.out.println("Experience: " + experienceYears + " years");
    }
}