package com.example.weysi.firabaseuserregistration.informations;

import android.graphics.Bitmap;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

/**
 * Created by weysi on 6.11.2017.
 */

public class CheckInInformation {
    private String placeID;
    private String placeName;
    private String userName;
    private String userId;
    private String message;
    private String checkInId;
    private Long checkInTime;
    private Bitmap userPhoto;

    public CheckInInformation(String placeID, String placeName, String userName, String userId,String message,String checkInId, Long checkInTime, Bitmap userPhoto) {
        this.placeID = placeID;
        this.placeName = placeName;
        this.userName = userName;
        this.message = message;
        this.checkInTime = checkInTime;
        this.userPhoto = userPhoto;
        this.userId=userId;
        this.checkInId=checkInId;
    }
    public CheckInInformation(){

    }

    @Exclude
    public Long getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(Long checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getCheckInId() {
        return checkInId;
    }

    public void setCheckInId(String checkInId) {
        this.checkInId = checkInId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public Bitmap getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(Bitmap userPhoto) {
        this.userPhoto = userPhoto;
    }


}
