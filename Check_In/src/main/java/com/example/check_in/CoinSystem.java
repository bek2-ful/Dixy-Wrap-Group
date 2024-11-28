package com.example.check_in;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CoinSystem {
    private int coins; //total coins in acc
    private LocalDate lastCheckInDate; //last check-in date
    private List<String> transactionHistory; //store transaction history

    public CoinSystem() {
        this.coins = 0;
        this.lastCheckInDate = LocalDate.now().minusDays(1); //initialize to yesterday
        this.transactionHistory = new ArrayList<>();
    }

    //method for daily check-in
    public void checkIn(int input) {
        LocalDate today = LocalDate.now();
        if (input == 0) {
            if (!lastCheckInDate.equals(today)) {
                coins += 50;
                lastCheckInDate = today;
                transactionHistory.add("Check-in + 50 coins on " + today);
                System.out.println("Check-in successful! You earned 50 coins!");
            } else {
                System.out.println("Coins claimed! Come again tomorrow!");
            }
        } else {
            System.out.println("Coins claimed! Come again tomorrow!");
        }
    }

    //Method to convert coins into a voucher
    public void redeemVoucher (int voucherValue) {
        if (coins >= voucherValue) {
            coins -= voucherValue;
            transactionHistory.add("Redeemed Voucher: -" + voucherValue + " coins on " + LocalDate.now());
            System.out.println("Voucher redeemed successfully!");
        } else {
            System.out.println("Not enough coins to redeem this voucher!");
        }
    }

    //Display transaction history
    public void displayTransactionHistory() {
        System.out.println("\nTransaction history:");
        if (transactionHistory.isEmpty()) {
            System.out.println("No transaction history available!");
        } else {
            for (String transaction : transactionHistory) {
                System.out.println(transaction);
            }
        }
    }

    //display current coin balance
    public void displayCoins() {
        System.out.println("\nCurrent coins: " + coins);
    }
}
