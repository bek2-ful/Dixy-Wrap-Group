package com.example.prototypes;

public class Voucher {
    private int voucherId;
    private String reward;
    private int points_needed;
    private String company;
    private String logo_path;

    public Voucher(int voucherId, String reward, int points_needed, String company, String logo_path){
        this.voucherId = voucherId;
        this.reward = reward;
        this.points_needed = points_needed;
        this.company = company;
        this.logo_path = logo_path;
    }

    public String getReward() {
        return reward;
    }

    public int getPoints_needed() {
        return points_needed;
    }

    public String getCompany() {
        return company;
    }

    public String getLogo_path() {
        return logo_path;
    }


}
