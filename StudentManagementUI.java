package student_management;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class StudentManagementUI extends JFrame {

    private JTextField nameField, courseField, ageField, ratingField, contactField, emailField, qualificationField, subjectsField;
    private JTextArea feedbackArea;
    private JButton addButton;

    // Admin login fields
    private JTextField adminUserField;
    private JPasswordField adminPassField;
    private JButton adminLoginButton;

    public StudentManagementUI() {

        setTitle("Student Survey & Admin Management System");
        setSize(950, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2));

        // ================= LEFT SIDE = STUDENT FORM PANEL ===================
        JPanel studentPanel = new JPanel(new GridLayout(11, 2, 10, 10));
        studentPanel.setBorder(BorderFactory.createTitledBorder("Student Survey Form"));

        nameField = new JTextField();
        courseField = new JTextField();
        ageField = new JTextField();
        ratingField = new JTextField();
        feedbackArea = new JTextArea(2, 20);
        contactField = new JTextField();
        emailField = new JTextField();
        qualificationField = new JTextField();
        subjectsField = new JTextField();

        studentPanel.add(new JLabel("Name:"));
        studentPanel.add(nameField);

        studentPanel.add(new JLabel("Course:"));
        studentPanel.add(courseField);

        studentPanel.add(new JLabel("Age:"));
        studentPanel.add(ageField);

        studentPanel.add(new JLabel("Rating (1-5):"));
        studentPanel.add(ratingField);

        studentPanel.add(new JLabel("Feedback:"));
        studentPanel.add(new JScrollPane(feedbackArea));

        studentPanel.add(new JLabel("Contact Number:"));
        studentPanel.add(contactField);

        studentPanel.add(new JLabel("Email:"));
        studentPanel.add(emailField);

        studentPanel.add(new JLabel("Qualification:"));
        studentPanel.add(qualificationField);

        studentPanel.add(new JLabel("Interested Subjects:"));
        studentPanel.add(subjectsField);

        addButton = new JButton("Add Student");
        studentPanel.add(addButton);

        add(studentPanel);

        // ACTION
        addButton.addActionListener(e -> addStudent());

        // =============== RIGHT SIDE = ADMIN LOGIN PANEL ====================
        JPanel adminPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        adminPanel.setBorder(BorderFactory.createTitledBorder("Admin Login"));

        adminUserField = new JTextField();
        adminPassField = new JPasswordField();
        adminLoginButton = new JButton("Login");

        adminPanel.add(new JLabel("Admin Username:"));
        adminPanel.add(adminUserField);

        adminPanel.add(new JLabel("Admin Password:"));
        adminPanel.add(adminPassField);

        adminPanel.add(adminLoginButton);

        add(adminPanel);

        // LOGIN ACTION
        adminLoginButton.addActionListener(e -> checkAdminLogin());
    }

    // ==================== ADD STUDENT ======================
    private void addStudent() {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "INSERT INTO student(name, course, age, rating, feedback, contact, email, qualification, subjects) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, nameField.getText());
            stmt.setString(2, courseField.getText());
            stmt.setInt(3, Integer.parseInt(ageField.getText()));
            stmt.setInt(4, Integer.parseInt(ratingField.getText()));
            stmt.setString(5, feedbackArea.getText());
            stmt.setString(6, contactField.getText());
            stmt.setString(7, emailField.getText());
            stmt.setString(8, qualificationField.getText());
            stmt.setString(9, subjectsField.getText());

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Student Added Successfully!");

            clearFields();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void clearFields() {
        nameField.setText("");
        courseField.setText("");
        ageField.setText("");
        ratingField.setText("");
        feedbackArea.setText("");
        contactField.setText("");
        emailField.setText("");
        qualificationField.setText("");
        subjectsField.setText("");
    }

    // ==================== ADMIN LOGIN ======================
    private void checkAdminLogin() {

        String user = adminUserField.getText();
        String pass = new String(adminPassField.getPassword());

        boolean loggedIn = Admin.login(user, pass);

        if (loggedIn) {
            JOptionPane.showMessageDialog(this, "Login Successful!");
            new AdminDashboard().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Username or Password!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentManagementUI().setVisible(true));
    }
}
