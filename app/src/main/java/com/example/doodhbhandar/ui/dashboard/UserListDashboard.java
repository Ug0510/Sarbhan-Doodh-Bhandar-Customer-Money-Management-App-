package com.example.doodhbhandar.ui.dashboard;

public class UserListDashboard {


    private String name;
   String date;
    private String soldAmount;
    private String takenAmount;

    public UserListDashboard(String name, String date, String soldAmount, String takenAmount) {
        this.name = name;
        this.date = date;
        this.soldAmount = soldAmount;
        this.takenAmount = takenAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSoldAmount() {
        return soldAmount;
    }

    public void setSoldAmount(String soldAmount) {
        this.soldAmount = soldAmount;
    }

    public String getTakenAmount() {
        return takenAmount;
    }

    public void setTakenAmount(String takenAmount) {
        this.takenAmount = takenAmount;
    }


}
