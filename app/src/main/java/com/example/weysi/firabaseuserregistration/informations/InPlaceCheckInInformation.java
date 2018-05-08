package com.example.weysi.firabaseuserregistration.informations;

/**
 * Created by Enes on 9.05.2018.
 */

public class InPlaceCheckInInformation {
    private String name;
    private String userID;
    private String cinsiyet;
    private Long checkInTime;

    public InPlaceCheckInInformation() {
    }

    public InPlaceCheckInInformation(String name, String userID, String cinsiyet, Long checkInTime) {
        this.name = name;
        this.userID = userID;
        this.cinsiyet = cinsiyet;
        this.checkInTime = checkInTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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
