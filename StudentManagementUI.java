package student_management;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class StudentManagementUI extends JFrame {
    private JTextField nameField, courseField, ageField;
    private JButton addButton, viewButton, deleteButton;
    private JTextArea outputArea;

    public StudentManagementUI() {
        setTitle("Student Management System");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 🔹 Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add Student"));

        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Course:"));
        courseField = new JTextField();
        inputPanel.add(courseField);

        inputPanel.add(new JLabel("Age:"));
        ageField = new JTextField();
        inputPanel.add(ageField);

        addButton = new JButton("Add Student");
        inputPanel.add(addButton);

        add(inputPanel, BorderLayout.NORTH);

        // 🔹 Output Area
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        // 🔹 Button Panel
        JPanel buttonPanel = new JPanel();
        viewButton = new JButton("View Students");
        deleteButton = new JButton("Delete Student");
        buttonPanel.add(viewButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // 🔹 Button actions
        addButton.addActionListener(e -> addStudent());
        viewButton.addActionListener(e -> viewStudents());
        deleteButton.addActionListener(e -> deleteStudent());
    }

    private void addStudent() {
        String name = nameField.getText();
        String course = courseField.getText();
        String ageText = ageField.getText();

        if (name.isEmpty() || course.isEmpty() || ageText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO students(name, course, age) VALUES (?, ?, ?)")) {

            stmt.setString(1, name);
            stmt.setString(2, course);
            stmt.setInt(3, Integer.parseInt(ageText));
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Student added successfully!");
            nameField.setText("");
            courseField.setText("");
            ageField.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void viewStudents() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM students")) {

            outputArea.setText("ID\tName\tCourse\tAge\n-----------------------------------\n");
            while (rs.next()) {
                outputArea.append(rs.getInt("id") + "\t" +
                        rs.getString("name") + "\t" +
                        rs.getString("course") + "\t" +
                        rs.getInt("age") + "\n");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void deleteStudent() {
        String id = JOptionPane.showInputDialog(this, "Enter student ID to delete:");
        if (id == null || id.isEmpty()) return;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM students WHERE id = ?")) {

            stmt.setInt(1, Integer.parseInt(id));
            int rows = stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, rows > 0 ? "Student deleted!" : "ID not found!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentManagementUI().setVisible(true));
    }
}
