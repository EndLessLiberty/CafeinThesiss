package com.example.weysi.firabaseuserregistration.informations;

import android.graphics.Bitmap;

/**
 * Created by Enes on 1.05.2018.
 */

public class TimeLineCheckInInformation {
    private String placeID;
    private String placeName;
    private String userName;
    private String checkInID;
    private String userID;
    private String message;
    private long checkInTime;
    private String userPhoto;


    public TimeLineCheckInInformation(String placeID, String placeName, String userName, String checkInID, String userID, String message, long checkInTime, String userPhoto) {
        this.placeID = placeID;
        this.placeName = placeName;
        this.userName = userName;
        this.checkInID = checkInID;
        this.userID = userID;
        this.message = message;
        this.checkInTime = checkInTime;
        this.userPhoto = userPhoto;
    }


    public TimeLineCheckInInformation()
    {

    }


    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public String getCheckInID() {
        return checkInID;
    }

    public void setCheckInID(String checkInID) {
        this.checkInID = checkInID;
    }

    public String getUserId() {
        return userID;
    }

    public void setUserId(String userId) {
        this.userID = userId;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(Long checkInTime) {
        this.checkInTime = checkInTime;
    }

}
