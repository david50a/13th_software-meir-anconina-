package com.example;
import java.sql.*;

public class DatabaseFunctions {
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=NORTHWND;integratedSecurity=true;encrypt=true;trustServerCertificate=true";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                System.out.println("Connected with Windows Authentication.");

                selectCustomers(conn);
                updateCustomer(conn, "ALFKI", "Updated Company Name"); // Assuming 'ALFKI' is a CustomerID
                deleteCustomer(conn, "ANATR"); // Assuming 'ANATR' is a CustomerID
                System.out.println("\nCustomers after updates and deletion:");
                selectCustomers(conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void selectCustomers(Connection conn) throws SQLException {
        String query = "SELECT CustomerID, CompanyName, ContactName FROM Customers"; // Adjust column names
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("Customers in database:");
            while (rs.next()) {
                System.out.printf("ID: %s, Company: %s, Contact: %s%n",
                        rs.getString("CustomerID"), // Adjust column name
                        rs.getString("CompanyName"), // Adjust column name
                        rs.getString("ContactName")); // Adjust column name
            }
        }
    }

    private static void updateCustomer(Connection conn, String customerId, String companyName) throws SQLException {
        String query = "UPDATE Customers SET CompanyName = ? WHERE CustomerID = ?"; // Adjust column names
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, companyName);
            pstmt.setString(2, customerId);
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Updated rows: " + rowsAffected);
        }
    }

    private static void deleteCustomer(Connection conn, String customerId) throws SQLException {
        // First, delete any associated orders
        String deleteOrdersQuery = "DELETE FROM Orders WHERE CustomerID = ?";
        try (PreparedStatement pstmtOrders = conn.prepareStatement(deleteOrdersQuery)) {
            pstmtOrders.setString(1, customerId);
            int ordersDeleted = pstmtOrders.executeUpdate();
            System.out.println("Deleted orders for CustomerID " + customerId + ": " + ordersDeleted);
        }

        // Then, delete the customer
        String deleteCustomerQuery = "DELETE FROM Customers WHERE CustomerID = ?";
        try (PreparedStatement pstmtCustomer = conn.prepareStatement(deleteCustomerQuery)) {
            pstmtCustomer.setString(1, customerId);
            int customersDeleted = pstmtCustomer.executeUpdate();
            System.out.println("Deleted customers: " + customersDeleted);
        }
    }
}