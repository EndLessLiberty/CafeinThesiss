package com.example.weysi.firabaseuserregistration.informations;

/**
 * Created by Enes on 13.05.2018.
 */

public class StatisticInformation {

    private String UserID;
    private String name;
    private String cinsiyet;
    private Long checkInTime;

    public StatisticInformation(String userID, String name, String cinsiyet, Long checkInTime) {
        UserID = userID;
        this.name = name;
        this.cinsiyet = cinsiyet;
        this.checkInTime = checkInTime;
    }

    public StatisticInformation() {

    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCinsiyet() {
        return cinsiyet;
    }

    public void setCinsiyet(String cinsiyet) {
        this.cinsiyet = cinsiyet;
    }

    public Long getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(Long checkInTime) {
        this.checkInTime = checkInTime;
    }
}
