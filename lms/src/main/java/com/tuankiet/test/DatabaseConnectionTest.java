package com.tuankiet.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.io.InputStream;

/**
 * Simple database connection test utility.
 * 
 * @author tuankiet
 * @version 1.0.0
 * @since 1.0.0
 */
public class DatabaseConnectionTest {

    public static void main(String[] args) {
        System.out.println("=== Database Connection Test ===");
        
        try {
            // Load database properties
            Properties props = new Properties();
            try (InputStream input = DatabaseConnectionTest.class.getClassLoader()
                    .getResourceAsStream("database.properties")) {
                if (input == null) {
                    System.out.println("❌ Unable to find database.properties");
                    return;
                }
                props.load(input);
            }

            String url = props.getProperty("db.url");
            String username = props.getProperty("db.username");
            String password = props.getProperty("db.password");

            System.out.println("🔗 Connection Details:");
            System.out.println("   URL: " + url);
            System.out.println("   Username: " + username);
            System.out.println("   Password: " + (password != null ? "***" : "null"));

            System.out.println("🔄 Testing connection...");

            // Test connection
            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                System.out.println("✅ Database connection successful!");
                
                // Get database info
                System.out.println("   Database: " + connection.getCatalog());
                System.out.println("   Driver: " + connection.getMetaData().getDriverName());
                System.out.println("   Version: " + connection.getMetaData().getDriverVersion());

                // Test a simple query
                try (Statement stmt = connection.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT GETDATE() as server_time")) {
                    if (rs.next()) {
                        System.out.println("   Server Time: " + rs.getTimestamp("server_time"));
                    }
                }

                System.out.println("🎉 All tests passed!");

            } catch (Exception e) {
                System.out.println("❌ Database connection failed!");
                System.out.println("   Error: " + e.getMessage());
                System.out.println("   SQL State: " + 
                    (e instanceof java.sql.SQLException ? ((java.sql.SQLException) e).getSQLState() : "N/A"));
                System.out.println("   Error Code: " + 
                    (e instanceof java.sql.SQLException ? ((java.sql.SQLException) e).getErrorCode() : "N/A"));
                
                System.out.println("🔧 Troubleshooting tips:");
                System.out.println("   1. Make sure SQL Server is running");
                System.out.println("   2. Check if database 'LibraryDB' exists");
                System.out.println("   3. Verify username/password: " + username + "/" + password);
                System.out.println("   4. Check firewall settings");
                System.out.println("   5. Ensure SQL Server allows SQL authentication");
            }

        } catch (Exception e) {
            System.out.println("❌ Failed to load configuration: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
