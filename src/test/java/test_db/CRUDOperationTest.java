package test_db;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class CRUDOperationTest {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/testdb";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "C.mohan@81";

    private static Connection connection;

    @BeforeAll
    public static void setUp() throws SQLException {
        connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        String createTableSQL = "CREATE TABLE IF NOT EXISTS users (id INT PRIMARY KEY, name VARCHAR(100), email VARCHAR(100))";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        }
    }

    @AfterEach
    public void tearDown() throws SQLException {
        String dropTableSQL = "DROP TABLE IF EXISTS users";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(dropTableSQL);
        }
        connection.close();
    }

    @Test
    public void testInsertData() throws SQLException {
        String insertSQL = "INSERT INTO users (id, name, email) VALUES (1, 'Chandramohan', 'chandramohan@gmail.com')";
        try (Statement stmt = connection.createStatement()) {
            int rowsInserted = stmt.executeUpdate(insertSQL);
            assertEquals(1, rowsInserted, "Insert operation failed");
        }
    }

    @Test
    public void testReadData() throws SQLException {
        String insertSQL = "INSERT INTO users (id, name, email) VALUES (1, 'Chandramohan', 'chandramohan@gmail.com')";
        String selectSQL = "SELECT * FROM users WHERE id = 1";

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(insertSQL);
            try (ResultSet rs = stmt.executeQuery(selectSQL)) {
                assertTrue(rs.next(), "No data found");
                assertEquals(1, rs.getInt("id"));
                assertEquals("Chandramohan", rs.getString("name"));
                assertEquals("chandramohan@gmail.com", rs.getString("email"));
            }
        }
    }

    @Test
    public void testUpdateData() throws SQLException {
        String insertSQL = "INSERT INTO users (id, name, email) VALUES (1, 'Chandramohan', 'chandramohan@gmail.com')";
        String updateSQL = "UPDATE users SET name = 'Venkatesh', email = 'venkatesh@gmail.com' WHERE id = 1";
        String selectSQL = "SELECT * FROM users WHERE id = 1";

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(insertSQL);
            int rowsUpdated = stmt.executeUpdate(updateSQL);
            assertEquals(1, rowsUpdated, "Update operation failed");

            try (ResultSet rs = stmt.executeQuery(selectSQL)) {
                assertTrue(rs.next(), "No data found");
                assertEquals("Venkatesh", rs.getString("name"));
                assertEquals("venkatesh@gmail.com", rs.getString("email"));
            }
        }
    }

    @Test
    public void testDeleteData() throws SQLException {
        String insertSQL = "INSERT INTO users (id, name, email) VALUES (1, 'Chandramohan', 'chandramohan@gmail.com')";
        String deleteSQL = "DELETE FROM users WHERE id = 1";
        String selectSQL = "SELECT * FROM users WHERE id = 1";

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(insertSQL);
            int rowsDeleted = stmt.executeUpdate(deleteSQL);
            assertEquals(1, rowsDeleted, "Delete operation failed");

            try (ResultSet rs = stmt.executeQuery(selectSQL)) {
                assertFalse(rs.next(), "Data not deleted");
            }
        }
    }
}
