package com.example.prototypes;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbFunction {

    /* function needed to get from database
    *  current point from user_points table
    * voucher from voucher id
    * claim voucher
    * points deduction and points addition
    * display transaction
    *
    * */
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
            String query = "SELECT reward, points_needed, company, logo_path FROM voucher";
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            while (rs.next()) {
                String reward = rs.getString("reward");
                int points_needed = rs.getInt("points_needed");
                String company = rs.getString("company");
                String logo_path = rs.getString("logo_path");

                vouchers.add(new Voucher(reward,points_needed,company,logo_path));
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

                System.out.println(date + name + points_earned + points_spent);

                transactions.add(new Transaction(date, name, points_earned, points_spent));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return transactions;
    }

}
