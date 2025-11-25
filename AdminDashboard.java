package student_management;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AdminDashboard extends JFrame {

    private JTextArea outputArea;

    public AdminDashboard() {

        setTitle("Admin Dashboard");
        setSize(650, 600);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4));

        JButton viewBtn = new JButton("View Students");
        JButton avgRatingBtn = new JButton("Average Rating");
        JButton courseCountBtn = new JButton("Course Count");
        JButton delBtn = new JButton("Delete Student");

        buttonPanel.add(viewBtn);
        buttonPanel.add(avgRatingBtn);
        buttonPanel.add(courseCountBtn);
        buttonPanel.add(delBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        // ACTION LISTENERS
        viewBtn.addActionListener(e -> viewStudents());
        avgRatingBtn.addActionListener(e -> averageRating());
        courseCountBtn.addActionListener(e -> courseWiseCount());
        delBtn.addActionListener(e -> deleteStudent());
    }

    private void viewStudents() {
        try {
            Connection conn = DBConnection.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM student");

            outputArea.setText("ID | Name | Course | Age | Rating | Contact\n-------------------------------------------------\n");

            while (rs.next()) {
                outputArea.append(
                        rs.getInt("id") + " | " +
                        rs.getString("name") + " | " +
                        rs.getString("course") + " | " +
                        rs.getInt("age") + " | " +
                        rs.getInt("rating") + " | " +
                        rs.getString("contact") + "\n"
                );
            }

        } catch (Exception ex) {
            outputArea.setText("ERROR: " + ex.getMessage());
        }
    }

    private void averageRating() {
        try {
            Connection conn = DBConnection.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT AVG(rating) AS avg_rating FROM student");

            if (rs.next()) {
                outputArea.setText("Average Rating = " + rs.getFloat("avg_rating"));
            }

        } catch (Exception ex) {
            outputArea.setText("ERROR: " + ex.getMessage());
        }
    }

    private void courseWiseCount() {
        try {
            Connection conn = DBConnection.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT course, COUNT(*) AS total FROM student GROUP BY course");

            outputArea.setText("--- Course Wise Count ---\n");
            while (rs.next()) {
                outputArea.append(rs.getString("course") + " = " + rs.getInt("total") + "\n");
            }

        } catch (Exception ex) {
            outputArea.setText("ERROR: " + ex.getMessage());
        }
    }

    private void deleteStudent() {
        String id = JOptionPane.showInputDialog(this, "Enter Student ID to delete:");

        if (id == null || id.isEmpty()) return;

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM student WHERE id=?");
            ps.setInt(1, Integer.parseInt(id));

            int rows = ps.executeUpdate();

            JOptionPane.showMessageDialog(this, rows > 0 ? "Student Deleted!" : "Invalid ID");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "ERROR: " + ex.getMessage());
        }
    }
}
