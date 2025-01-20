package test_db;

import java.sql.*;

public class ProjectsJDBC {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/testdb";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "C.mohan@81";

    public static void main(String[] args) {

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {

            createProjectsTable(connection);

            insertProject(connection, "Project Alpha", 1, "ongoing");
            insertProject(connection, "Project Beta", 2, "completed");
            insertProject(connection, "Project Gamma", 3, "ongoing");

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    private static void createProjectsTable(Connection connection) throws SQLException {

        String createTableSQL = "CREATE TABLE IF NOT EXISTS projects (project_id INT AUTO_INCREMENT PRIMARY KEY,project_name VARCHAR(100) NOT NULL,department_id INT NOT NULL,status VARCHAR(20) DEFAULT 'ongoing',FOREIGN KEY (department_id) REFERENCES Departments(department_id))";

        try (Statement statement = connection.createStatement()) {

            statement.execute(createTableSQL);
            System.out.println("Projects table created successfully.");
        }
    }

    private static void insertProject(Connection connection, String projectName, int departmentId, String status) throws SQLException {

        String insertSQL = "INSERT INTO projects (project_name, department_id, status) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {

            pstmt.setString(1, projectName);
            pstmt.setInt(2, departmentId);
            pstmt.setString(3, status);
            pstmt.executeUpdate();
            System.out.println("Inserted project: " + projectName);
        }
        catch (SQLException e) {

            System.out.println("Error inserting project: " + projectName);
            throw e;
        }
    }
}
