package com.example.prototypes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
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

    public String read_transactions (Connection conn) {
        Statement statement;
        ResultSet rs = null;
        String result = null;

        try {
            String query = "SELECT * FROM transaction WHERE user_id = ? ORDER BY date DESC LIMIT 5";
            statement = conn.createStatement();
            rs = statement.executeQuery(query);

            while(rs.next()){
                result = rs.getString("date") + rs.getString("transaction_name" + rs.getString("points_earned"));

            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }


    public void createTable(Connection conn, String table_name) {
        Statement statement;
        try {
            String query = "create table " + table_name + "(empid SERIAL, name varchar(200), address varchar(200), primary key(empid));";
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Table Created");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void insert_row(Connection conn, String tablename, String name, String address) {
        Statement statement;
        try {
            String query = String.format("insert into %s(name, address) values('%s', '%s');", tablename, name, address);
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Row inserted");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void read_data(Connection conn, String tablename) {
        Statement statement;
        ResultSet rs = null;
        try {
            String query = String.format("select * from %s", tablename);
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            while (rs.next()) {
                System.out.print(rs.getString("empid") + " ");
                System.out.print(rs.getString("name") + " ");
                System.out.println(rs.getString("address") + " ");
            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void update_name(Connection conn, String tablename, String old_name, String new_name) {
        Statement statement;
        try {
            String query = String.format("update %s set name = '%s' where name = '%s'", tablename, new_name, old_name);
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Data updated");
        } catch (Exception e) {

        }

    }

    public void search_by_name(Connection conn, String tablename, String name) {
    Statement statement;
    ResultSet rs = null;

        try {
            String query = String.format("select * from %s where name = '%s' ", tablename, name);
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            while (rs.next()){
                System.out.print(rs.getString("empid") + " ");
                System.out.print(rs.getString("name") + " ");
                System.out.println(rs.getString("address") + " ");
            }
        } catch(Exception e) {
            System.out.println(e);
        }

    }
    public void search_by_id(Connection conn, String tablename, int id) {
        Statement statement;
        ResultSet rs = null;

        try {
            String query = String.format("select * from %s where empid = %s ", tablename, id);
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            while (rs.next()){
                System.out.print(rs.getString("empid") + " ");
                System.out.print(rs.getString("name") + " ");
                System.out.println(rs.getString("address") + " ");
            }
        } catch(Exception e) {
            System.out.println(e);
        }

    }

    public void delete_row (Connection conn, String tablename, String name){
        Statement statement;
        try{
            String query = String.format("delete from %s where name = '%s'", tablename, name);
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Data deleted");
        } catch(Exception e){
            System.out.println(e);
        }
    }

}
