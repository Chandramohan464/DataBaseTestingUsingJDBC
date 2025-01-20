package test_db;

import java.sql.*;

public class EmployeesJDBC {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/testdb";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "C.mohan@81";

    public static void main(String[] args) {

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {

            createEmployeesTable(connection);

            insertEmployee(connection, "Chandramohan", "Halemani", 1, 60000, "2023-01-15");
            insertEmployee(connection, "Abhishek", "Itagimath", 2, 75000, "2022-07-10");
            insertEmployee(connection, "Venkatesh", "Manvi", 3, 50000, "2024-03-20");
        }
        catch (SQLException e){

            e.printStackTrace();
        }
    }

    private static void createEmployeesTable(Connection connection) throws SQLException {

        String createTableSQL = "CREATE TABLE IF NOT EXISTS Employees (employee_id INT AUTO_INCREMENT PRIMARY KEY,first_name VARCHAR(50) NOT NULL,last_name VARCHAR(50) NOT NULL,department_id INT NOT NULL,salary DECIMAL(10, 2) NOT NULL,joining_date DATE NOT NULL,FOREIGN KEY (department_id) REFERENCES Departments(department_id))";

        try (Statement statement = connection.createStatement()) {

            statement.execute(createTableSQL);
            System.out.println("Employees table created successfully.");
        }
    }

    private static void insertEmployee(Connection connection, String firstName, String lastName, int departmentId, double salary, String joiningDate) throws SQLException {

        String insertSQL = "INSERT INTO employees (first_name, last_name, department_id, salary, joining_date) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {

            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setInt(3, departmentId);
            pstmt.setDouble(4, salary);
            pstmt.setString(5, joiningDate);
            pstmt.executeUpdate();
            System.out.println("Inserted employee: " + firstName + " " + lastName);
        } catch (SQLException e) {

            System.out.println("Error inserting employee: " + firstName + " " + lastName);
            throw e;
        }
    }
}
