package com.example.gameoflife.controller;

import java.sql.*;

public class database {
    private static final String DB_URL ="jdbc:sqlserver://localhost:1433;databaseName=GAME_OF_LIFE;integratedSecurity=true;encrypt=true;trustServerCertificate=true";
    private Connection conn = null;

    public database() {
        try {
            conn = DriverManager.getConnection(DB_URL);
            if (conn != null) {
                System.out.println("The connection has been established");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected Connection getConnection() {
        return this.conn;
    }

    protected static int startInsert(Connection c, boolean[][] matrix) {
        String query = "INSERT INTO [START](HEIGHT, WIDTH) VALUES (?, ?)";
        int id = -1;
        try (PreparedStatement statement = c.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, matrix.length);
            statement.setInt(2, matrix[0].length);
            statement.executeUpdate();

            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    id = rs.getInt(1);
                } else {
                    throw new SQLException("Failed to retrieve generated key.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    protected static int matrixInsert(Connection c, int id, int step, boolean[][] matrix) {
        String matrixQuery = "INSERT INTO MATRIX(STEP, BOARD_ID, X, Y) VALUES (?, ?, ?, ?)";
        try (PreparedStatement position = c.prepareStatement(matrixQuery)) {
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[0].length; j++) {
                    if (matrix[i][j]) {
                        position.setInt(1, step);
                        position.setInt(2, id);
                        position.setInt(3, i);
                        position.setInt(4, j);
                        position.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }
    protected static boolean[][] selectStep(Connection c, int id, int step) {
        String query = "SELECT X, Y FROM MATRIX WHERE BOARD_ID = ? AND STEP = ?";
        String size = "SELECT WIDTH, HEIGHT FROM [START] WHERE ID = ?";

        try (
                PreparedStatement select = c.prepareStatement(query);
                PreparedStatement s = c.prepareStatement(size)
        ) {
            // Get board dimensions
            s.setInt(1, id);
            int width = 1, height = 1;
            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {
                    width = r.getInt("WIDTH");
                    height = r.getInt("HEIGHT");
                }
            }

            // Initialize matrix
            boolean[][] matrix = new boolean[width][height];

            // Get matrix positions
            select.setInt(1, id);
            select.setInt(2, step);
            try (ResultSet rs = select.executeQuery()) {
                while (rs.next()) {
                    int x = rs.getInt("X");
                    int y = rs.getInt("Y");

                    // Safety check to avoid out-of-bounds access
                    if (x >= 0 && x < width && y >= 0 && y < height) {
                        matrix[x][y] = true;
                    }
                }
            }
            return matrix;
        } catch (SQLException e) {
            e.printStackTrace(); // Or use proper logging
            return null; // Or consider throwing an exception
        }
    }

}
