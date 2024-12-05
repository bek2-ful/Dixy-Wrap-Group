package com.example.prototypes;

import java.sql.Date;

public class Transaction {
    private Date date ;
    private String name;
    private int points_earned;
    private int points_spent;

    public Transaction(Date date, String name, int points_earned, int points_spent){
        this.date = date;
        this.name = name;
        this.points_earned = points_earned;
        this.points_spent = points_spent;
    }

    public Date getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public int getPoints_earned() {
        return points_earned;
    }

    public int getPoints_spent() {
        return points_spent;
    }

}
