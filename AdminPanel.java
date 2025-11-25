package student_management;

import java.sql.*;
import java.util.Scanner;

public class AdminPanel {

    Scanner sc = new Scanner(System.in);

    public void showMenu() {
        while (true) {
            System.out.println("\n===== ADMIN DASHBOARD =====");
            System.out.println("1. View All Students");
            System.out.println("2. View Course-wise Student Count");
            System.out.println("3. View Average Rating");
            System.out.println("4. View All Feedback");
            System.out.println("5. Delete Student");
            System.out.println("6. Logout");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> viewStudents();
                case 2 -> courseWiseCount();
                case 3 -> averageRating();
                case 4 -> viewFeedback();
                case 5 -> deleteStudent();
                case 6 -> { return; }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    void viewStudents() {
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM students")) {

            System.out.println("\n--- ALL STUDENTS ---");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + " | " +
                        rs.getString("name") + " | " +
                        rs.getString("course") + " | Age: " +
                        rs.getInt("age") + " | Rating: " +
                        rs.getInt("rating"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void courseWiseCount() {
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT course, COUNT(*) AS total FROM students GROUP BY course"
             )) {

            System.out.println("\n--- COURSE WISE COUNT ---");
            while (rs.next()) {
                System.out.println(rs.getString("course") + " = " + rs.getInt("total"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void averageRating() {
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT AVG(rating) AS avg_rating FROM students")) {

            if (rs.next()) {
                System.out.println("\nAverage Rating = " + rs.getFloat("avg_rating"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void viewFeedback() {
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT name, course, feedback FROM students WHERE feedback IS NOT NULL"
             )) {

            System.out.println("\n--- STUDENT FEEDBACK ---");
            while (rs.next()) {
                System.out.println(rs.getString("name") + " (" +
                        rs.getString("course") + "): " +
                        rs.getString("feedback"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void deleteStudent() {
        System.out.print("Enter ID to delete: ");
        int id = sc.nextInt();

        try (Connection conn = DBConnection.getConnection()) {

            String sql = "DELETE FROM students WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            int rows = ps.executeUpdate();
            if (rows > 0)
                System.out.println("Student Deleted Successfully!");
            else
                System.out.println("Invalid Student ID!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
