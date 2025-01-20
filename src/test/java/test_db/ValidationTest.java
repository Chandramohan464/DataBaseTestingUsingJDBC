package test_db;

import junit.framework.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class ValidationTest {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/testdb";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "C.mohan@81";

    private static Connection connection;

    @BeforeAll
    static void setup() throws SQLException {
        connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

    @AfterAll
    static void teardown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    void testRetrieveAllEmployees() throws SQLException {
        String query = "SELECT * FROM Employees";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            int rowCount = 0;
            while (rs.next()) {
                rowCount++;
            }
            assertEquals(3, rowCount, "Employee row count should be 2");
        }
    }

    @Test
    void testInsertNewDepartment() throws SQLException {
        String query = "INSERT INTO Departments (department_name) VALUES ('Human Resources')";
        try (Statement stmt = connection.createStatement()) {
            int rowsAffected = stmt.executeUpdate(query);
            assertEquals(1, rowsAffected, "One row should be inserted");
        }
    }

    @Test
    void testUpdateEmployeeSalary() throws SQLException {
        String query = "UPDATE Employees SET salary = 60000 WHERE employee_id = 5";
        try (Statement stmt = connection.createStatement()) {
            int rowsAffected = stmt.executeUpdate(query);
            assertEquals(1, rowsAffected, "One row should be updated");

            try (ResultSet rs = stmt.executeQuery("SELECT salary FROM Employees WHERE employee_id = 5")) {
                assertTrue(rs.next(), "Result set should have one row");
                assertEquals(60000, rs.getInt("salary"), "Salary should be updated to 60000");
            }
        }
    }

    @Test
    void testDeleteOutdatedProject() throws SQLException {
        String query = "DELETE FROM Projects WHERE project_name = 'Outdated Project'";
        try (Statement stmt = connection.createStatement()) {
            int rowsAffected = stmt.executeUpdate(query);
            assertEquals(1, rowsAffected, "One row should be deleted");

            try (ResultSet rs = stmt.executeQuery("SELECT * FROM Projects WHERE project_name = 'Outdated Project'")) {
                assertFalse(rs.next(), "No rows should be returned");
            }
        }
    }

    @Test
    public void storeProcedureValidation() throws SQLException {
        String query = "{CALL Get_Employees_by_Id(?)}"; // Calling the stored procedure
        CallableStatement callableStatement = connection.prepareCall(query);
        callableStatement.setInt(1, 1); // Pass employee_id = 1

        ResultSet resultSet = callableStatement.executeQuery();

        // Verify if the result set contains the employee with id 1
        Assert.assertTrue("No employee found with ID 1 using CallableStatement", resultSet.next());
    }
}