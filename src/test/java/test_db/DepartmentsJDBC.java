package test_db;

import java.sql.*;

public class DepartmentsJDBC {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/testdb";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "C.mohan@81";

    public static void main(String[] args){

        try(Connection connection= DriverManager.getConnection(JDBC_URL,JDBC_USER,JDBC_PASSWORD)){

            createDepartmentsTable(connection);

            insertDepartment(connection, "Sales");
            insertDepartment(connection, "Engineering");
            insertDepartment(connection, "Human Resources");
        }
        catch (SQLException e){

            e.printStackTrace();
        }
    }

    private static void createDepartmentsTable(Connection connection) throws SQLException {

        String createTableSQL="CREATE TABLE IF NOT EXISTS departments(department_id INT AUTO_INCREMENT PRIMARY KEY,department_name VARCHAR(100) NOT NULL UNIQUE)";

        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
            System.out.println("Departments table created successfully.");
        }
    }

    private static void insertDepartment(Connection connection, String departmentName) throws SQLException {
        String insertSQL = "INSERT INTO departments (department_name) VALUES (?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setString(1, departmentName);
            pstmt.executeUpdate();
            System.out.println("Inserted department: " + departmentName);
        } catch (SQLException e) {
            System.out.println("Error inserting department: " + departmentName);
            throw e;
        }
    }
}
