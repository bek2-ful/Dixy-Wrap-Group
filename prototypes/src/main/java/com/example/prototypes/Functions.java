package com.example.prototypes;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class Functions {

    LocalDate localDate = LocalDate.now();

    public void addPoints (int userId, String dbname, String username, String password) {
        String add = "INSERT INTO transaction (user_id, transaction_date, points_earned, transaction_name) VALUES (?, ?, ?, ?)";

        DbFunction functions = new DbFunction();

        try (Connection conn = functions.connect_to_db(dbname, username, password)) {

            try (PreparedStatement addStatement = conn.prepareStatement(add)) {
                addStatement.setInt(2, userId);
                addStatement.setDate(3, java.sql.Date.valueOf(localDate));
                addStatement.setInt(5, 50);
                addStatement.setString(7, "Checked-In");

                int rowInserted = addStatement.executeUpdate();

                if (rowInserted == 1) {
                    System.out.println("Transaction successful");
                } else {
                    System.out.println("Transaction failed");
                }
            } catch (SQLException e) {
                e.printStackTrace();
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
