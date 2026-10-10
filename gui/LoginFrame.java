package gui;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    private final UserDAO userDAO;

    public LoginFrame() {
        userDAO = new UserDAO();

        setTitle("Online Health Consultation Platform - Login");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(
            BorderFactory.createEmptyBorder(25, 30, 25, 30)
        );

        // Heading
        JLabel headingLabel = new JLabel(
            "Online Health Consultation",
            SwingConstants.CENTER
        );
        headingLabel.setFont(
            new Font("Arial", Font.BOLD, 22)
        );

        JLabel subtitleLabel = new JLabel(
            "Please log in to continue",
            SwingConstants.CENTER
        );

        JPanel headingPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        headingPanel.add(headingLabel);
        headingPanel.add(subtitleLabel);

        // Login form
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 15));

        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);

        // Buttons
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");

        JPanel buttonPanel = new JPanel(
            new FlowLayout(FlowLayout.CENTER, 15, 5)
        );
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        // Arrange components
        mainPanel.add(headingPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Button actions
        loginButton.addActionListener(e -> login());

        registerButton.addActionListener(e -> {
            new RegisterFrame();
            dispose();
        });

        // Press Enter to log in
        getRootPane().setDefaultButton(loginButton);

        setVisible(true);
    }

    private void login() {

        String email = emailField.getText().trim();
        String password = new String(
            passwordField.getPassword()
        );

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Please enter both email and password.",
                "Missing Information",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            User user = userDAO.login(email, password);

            if (user == null) {
                JOptionPane.showMessageDialog(
                    this,
                    "Invalid email or password.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            String role = user.getRole();

            if (role == null) {
                JOptionPane.showMessageDialog(
                    this,
                    "Your account has no assigned role. Please contact the administrator.",
                    "Account Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // Open the dashboard according to the user's role
            switch (role.toUpperCase()) {

                case "PATIENT":
                    new PatientDashboard(user);
                    break;

                case "PROFESSIONAL":
                    new ProfessionalDashboard(user);
                    break;

                case "ADMIN":
                    new AdminDashboard(user);
                    break;

                default:
                    JOptionPane.showMessageDialog(
                        this,
                        "Unknown account role: " + role,
                        "Login Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
            }

            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this,
                "Unable to log in. Please check the database connection.\n"
                    + ex.getMessage(),
                "Login Error",
                JOptionPane.ERROR_MESSAGE
            );

            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}