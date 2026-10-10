package model;

public class Admin extends User {

    private int adminId;
    private String role;

    public Admin() {
        super();
    }

    public Admin(
            int userId,
            String fullName,
            String email,
            String password,
            String phone,
            int adminId,
            String role) {

        super(userId, fullName, email, password, phone);

        this.adminId = adminId;
        this.role = role;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public void displayInfo() {
        System.out.println("Admin: " + getFullName());
        System.out.println("Email: " + getEmail());
        System.out.println("Role: " + role);
    }
}