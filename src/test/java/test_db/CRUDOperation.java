package test_db;

import java.sql.*;

public class CRUDOperation {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/testdb";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "C.mohan@81";

    public static void main(String[] args) {

        try {

            Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

            createTable(connection);

            insertData(connection, 1, "Chandramohan", "chandramohan@gmail.com");
            insertData(connection, 2, "Abhishek", "abhishek@gmail.com");

            readData(connection);

            updateData(connection, 1, "Venkatesh", "venkatesh@gmail.com");

            deleteData(connection, 2);

            connection.close();
        }
        catch (SQLException e){

            e.printStackTrace();
        }
    }

    private static void createTable(Connection connection) throws SQLException{

        String createTableSQL = "CREATE TABLE IF NOT EXISTS users (id INT PRIMARY KEY,name VARCHAR(100),email VARCHAR(100))";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
            System.out.println("Table created successfully.");
        }
    }

    private static void insertData(Connection connection, int id, String name, String email) throws SQLException {
        String insertSQL = "INSERT INTO users (id, name, email) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, email);
            pstmt.executeUpdate();
            System.out.println("Data inserted: " + name);
        }
    }

    private static void readData(Connection connection) throws SQLException {
        String selectSQL = "SELECT * FROM users";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(selectSQL)) {

            System.out.println("User Data:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        ", Name: " + rs.getString("name") +
                        ", Email: " + rs.getString("email"));
            }
        }
    }

    private static void updateData(Connection connection, int id, String newName, String newEmail) throws SQLException {
        String updateSQL = "UPDATE users SET name = ?, email = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
            pstmt.setString(1, newName);
            pstmt.setString(2, newEmail);
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
            System.out.println("Data updated for ID: " + id);
        }
    }

    private static void deleteData(Connection connection, int id) throws SQLException {
        String deleteSQL = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Data deleted for ID: " + id);
        }
    }
}
