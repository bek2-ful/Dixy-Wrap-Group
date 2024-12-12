package com.example.prototypes;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.util.List;

public class DbFunctionTest {

    DbFunction db = new DbFunction();
    Connection conn = db.connect_to_db("gamification", "postgres", "fullstack24");

    // Test 1: Database Connection Test
    @Test
    public void testConnection() {
        Connection conn = db.connect_to_db("gamification", "postgres", "fullstack24");
        assertNotNull(conn, "Connection should be established");
    }

    // Test 2: Read Points Test
    @Test
    public void testReadPoints() {
        String points = db.read_points(conn, "user_points", 2);
        assertNotNull(points, "Points should not be null");
    }

    // Test 3: Read Vouchers Test
    @Test
    public void testReadVouchers() {
        List<Voucher> vouchers = db.read_voucher(conn);
        assertNotNull(vouchers, "Vouchers list should not be null");
        assertTrue(vouchers.size() > 0, "There should be at least one voucher in the list");
    }

    // Test 4: Add Points Test (Check-in)
    @Test
    public void testAddPoints() {
        db.addPoints(conn, 2);

        String updatePoints = db.read_points(conn, "user_points", 2);
        assertNotNull(updatePoints, "Updated points should not be null");
        assertEquals("1050", updatePoints, "Points should be 1050 after adding 50");
    }

    // Test 5: Redeem Voucher Test
    @Test
    public void testRedeemVoucher() {
        boolean success = db.redeemVoucher(conn, 2, 100);
        assertTrue(success, "Voucher redemption should be successful");

        String updatedPoints = db.read_points(conn, "user_points", 2);
        assertNotNull(updatedPoints, "Updated points should not be null");
        assertEquals("550", updatedPoints, "Points should be 550 after redeeming a voucher worth 500 points");
    }
}
