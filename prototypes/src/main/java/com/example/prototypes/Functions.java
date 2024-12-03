package com.example.prototypes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Functions {

    public void addPoints (int userId, String dbname, String username, String password) {
        String add = "UPDATE user_points SET current_points = current_points + 50 WHERE user_id = ?";

        DbFunction functions = new DbFunction();

        try (Connection conn = functions.connect_to_db(dbname, username, password);
        PreparedStatement stmt = conn.prepareStatement(add)) {
            stmt.setInt(1,userId);

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated == 1) {
                System.out.println("Points added");
            } else {
                System.out.println("No points were added");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void printPoints(int userId, String dbname, String username, String password) {
        String query = "SELECT current_points FROM user_points WHERE user_id = ?";
        DbFunction functions = new DbFunction();

        try (Connection conn = functions.connect_to_db(dbname, username, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set the user ID parameter
            stmt.setInt(1, userId);

            // Execute the SELECT query
            ResultSet rs = stmt.executeQuery();

            // Process the ResultSet
            if (rs.next()) { // Move the cursor to the first row of results
                int currentPoints = rs.getInt("current_points"); // Retrieve the value
                System.out.println("User ID " + userId + " has " + currentPoints + " points.");
            } else {
                System.out.println("No user found with ID: " + userId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
