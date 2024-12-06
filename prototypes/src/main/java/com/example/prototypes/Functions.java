package com.example.prototypes;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Functions {

    public void addPoints (int userId, String dbname, String username, String password) {
        String add = "UPDATE user_points SET current_points = current_points + 50 WHERE user_id = ?";
        String insertTrans = "INSERT INTO transaction (user_id, points_earned, points_spent, transaction_name) VALUES (?, ?, ?, ?)";

//        LocalDate localdate = LocalDate.now();
//        String date = localdate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        DbFunction functions = new DbFunction();

        try (Connection conn = functions.connect_to_db(dbname, username, password);
        PreparedStatement stmt = conn.prepareStatement(insertTrans)) {
            stmt.setInt(1,userId);

            stmt.setInt(1, userId);
            //stmt.setDate(3, Date.valueOf(date));// User ID
            //stmt.setTimestamp(2, currentTime); // Transaction date and time
            stmt.setInt(2, 50);            // Points earned
            stmt.setInt(3, 0);             // Points spent
            stmt.setString(4, "Checked In"); // Transaction name

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Points added");
            } else {
                System.out.println("No points were added");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void pointsToTransaction (int userId, String dbname, String username, String password) {

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
