package com.example.prototypes;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

public class DbFunction {

    /* function needed to get from database
    *  current point from user_points table
    * voucher from voucher id
    * claim voucher
    * points deduction and points addition
    * display transaction
    *
    * */

    Date currentDate = new Date(System.currentTimeMillis());
    Timestamp currentTime  = new Timestamp(System.currentTimeMillis());


    public Connection connect_to_db(String dbname, String username, String password) {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + dbname, username, password);
            if (conn != null) {
                System.out.println("Connection established");
            } else {
                System.out.println("Connection failed");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return conn;
    }


    // consider if still nak kena guna the tablename and id since it will all be the same table and id
    public String read_points(Connection conn, String table_name, int user_id) {
        Statement statement;
        ResultSet rs = null;
        try {
            String query = String.format("select * from %s where user_id = '%s' ", table_name, user_id);
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            while (rs.next()) {
                System.out.println(rs.getInt("current_points"));
                return String.valueOf(rs.getInt("current_points"));
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return "0";
    }

    public List<Voucher> read_voucher (Connection conn) {
        List<Voucher> vouchers = new ArrayList<>();
        Statement statement;
        ResultSet rs = null;
        try {
            String query = "SELECT voucher_id, reward, points_needed, company, logo_path FROM voucher";
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            while (rs.next()) {
                int voucher_id = rs.getInt("voucher_id");
                String reward = rs.getString("reward");
                int points_needed = rs.getInt("points_needed");
                String company = rs.getString("company");
                String logo_path = rs.getString("logo_path");

                vouchers.add(new Voucher(voucher_id,reward,points_needed,company,logo_path));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return vouchers;
    }
    public List<Transaction> read_transaction (Connection conn) {
        List<Transaction> transactions = new ArrayList<>();
        Statement statement;
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM transaction WHERE user_id = 1 ORDER BY transaction_date DESC LIMIT 5";
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            while (rs.next()) {
                Date date = rs.getDate("transaction_date");
                String name = rs.getString("transaction_name");
                int points_earned = rs.getInt("points_earned");
                int points_spent = rs.getInt("points_spent");

                transactions.add(new Transaction(date, name, points_earned, points_spent));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return transactions;
    }



    public void addPoints (Connection conn, int userId, String dbname, String username, String password) {

        try {
            String insertTrans = "INSERT INTO transaction (user_id,transaction_date, transaction_time, points_earned, points_spent, transaction_name) VALUES (?,?,?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(insertTrans);

            stmt.setInt(1, userId);
            stmt.setDate(2, currentDate);// User ID
            stmt.setTimestamp(3, currentTime); // Transaction date and time
            stmt.setInt(4, 50);            // Points earned
            stmt.setInt(5, 0);             // Points spent
            stmt.setString(6, "Check-In"); // Transaction name

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

    public boolean redeemVoucher (Connection conn, int userId, int voucherId, String dbname, String username, String password) {

        String userPoint = "SELECT current_points FROM user_points WHERE user_id = ?";
        String voucherPoint = "SELECT points_needed FROM voucher WHERE voucher_id = ?";
        String insertTrans = "INSERT INTO transaction (user_id,transaction_date, transaction_time, points_earned, points_spent, transaction_name) VALUES (?,?,?, ?, ?, ?)";

        boolean success = false;

        try {
            conn.setAutoCommit(false);

            int currentPoints;
            try (PreparedStatement userStmt = conn.prepareStatement(userPoint)) {
                userStmt.setInt(1, userId);
                try (ResultSet rs = userStmt.executeQuery()) {
                    if (rs.next()) {
                        currentPoints = rs.getInt("current_points");
                    } else {
                        System.out.println("User not found");
                        return false;
                    }
                }
            }

            int voucherPoints;
            try (PreparedStatement voucherStmt = conn.prepareStatement(voucherPoint)) {

                voucherStmt.setInt(1, voucherId);
                try (ResultSet rs = voucherStmt.executeQuery()) {
                    if (rs.next()) {
                        voucherPoints = rs.getInt("points_needed");
                    } else {
                        System.out.println("Voucher not found");
                        return false;
                    }
                }
            }

            if (currentPoints >= voucherPoints) {
                try (PreparedStatement insertStmt = conn.prepareStatement(insertTrans)) {
                    insertStmt.setInt(1, userId);
                    insertStmt.setDate(2, currentDate);// User ID
                    insertStmt.setTimestamp(3, currentTime); // Transaction date and time
                    insertStmt.setInt(4, 0);            // Points earned
                    insertStmt.setInt(5, voucherPoints);             // Points spent
                    insertStmt.setString(6, "Voucher Redeemed"); // Transaction name
                    insertStmt.executeUpdate();
                }
                conn.commit(); // Commit transaction
                success = true;
                System.out.println("Voucher redeemed successfully!");
            } else {
                System.out.println("Not enough points");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback transaction on error
                    System.out.println("Transaction rolled back.");
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }

        } finally {
            try {
                conn.setAutoCommit(true); // Reset auto-commit
            } catch (SQLException resetEx) {
                resetEx.printStackTrace();
            }
        }
        return success;
    }
}
