package student_management;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/studentdb?useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // empty since you removed password

    public static Connection getConnection() {
        Connection conn = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (ClassNotFoundException e) {
            System.out.println("❌ MySQL Driver NOT FOUND!");
            e.printStackTrace();
        }

        catch (SQLException e) {
            System.out.println("❌ SQL Error! Database connection failed.");
            e.printStackTrace();
        }

        return conn;
    }
}
