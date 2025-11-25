package student_management;
import java.sql.*;
import java.util.*;

public class StudentManagement {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== Student Survey & Management System =====");
            System.out.println("1. Add Student with Survey");
            System.out.println("2. View Students");
            System.out.println("3. Delete Student");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); // clear buffer

            switch (choice) {
                case 1 -> addStudent(sc);
                case 2 -> viewStudents();
                case 3 -> deleteStudent(sc);
                case 4 -> System.exit(0);
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    // **************** ADD STUDENT (WITH SURVEY) ****************
    static void addStudent(Scanner sc) {
        System.out.print("Enter name: ");
        String name = sc.nextLine();

        System.out.print("Enter course: ");
        String course = sc.nextLine();

        System.out.print("Enter age: ");
        int age = sc.nextInt();
        sc.nextLine();  // clear buffer

        System.out.print("Enter rating (1–5): ");
        int rating = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter feedback: ");
        String feedback = sc.nextLine();

        System.out.print("Enter contact number: ");
        String contact = sc.nextLine();

        System.out.print("Enter email: ");
        String email = sc.nextLine();

        System.out.print("Enter qualification: ");
        String qualification = sc.nextLine();

        System.out.print("Enter interested subjects (comma separated): ");
        String subjects = sc.nextLine();

        try (Connection conn = DBConnection.getConnection()) {

            String sql = "INSERT INTO students(name, course, age, rating, feedback, contact, email, qualification, subjects) "
                       + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, course);
            ps.setInt(3, age);
            ps.setInt(4, rating);
            ps.setString(5, feedback);
            ps.setString(6, contact);
            ps.setString(7, email);
            ps.setString(8, qualification);
            ps.setString(9, subjects);

            ps.executeUpdate();
            System.out.println("Student + Survey data added successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // **************** VIEW STUDENTS ****************
    static void viewStudents() {
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM students")) {

            System.out.println("\n--- Student List ---");
            while (rs.next()) {
                System.out.println(
                        rs.getInt("id") + " | " +
                        rs.getString("name") + " | " +
                        rs.getString("course") + " | " +
                        rs.getInt("age") + " | Rating: " +
                        rs.getInt("rating") + " | " +
                        rs.getString("feedback")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // **************** DELETE STUDENT ****************
    static void deleteStudent(Scanner sc) {
        System.out.print("Enter student ID to delete: ");
        int id = sc.nextInt();

        try (Connection conn = DBConnection.getConnection()) {

            String sql = "DELETE FROM students WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);
            int rows = ps.executeUpdate();

            if (rows > 0)
                System.out.println("Student deleted successfully!");
            else
                System.out.println("Student not found!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
