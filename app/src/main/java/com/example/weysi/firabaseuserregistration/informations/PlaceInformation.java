package com.example.weysi.firabaseuserregistration.informations;

/**
 * Created by weysi on 6.11.2017.
 */

public class PlaceInformation {
    private String placeID;
    private String name;
    private int maleCount;
    private int femaleCount;
   private String placeAddress;

    public String getPlaceAddress() {
        return placeAddress;
    }

    public void setPlaceAddress(String placeAddress) {
        this.placeAddress = placeAddress;
    }

    public int getMaleCount() {
        return maleCount;
    }

    public void setMaleCount(int maleCount) {
        this.maleCount = maleCount;
    }

    public int getFemaleCount() {
        return femaleCount;
    }

    public void setFemaleCount(int femaleCount) {
        this.femaleCount = femaleCount;
    }

    public PlaceInformation(String name, String placeID,int maleCount,int femaleCount, String placeAddress) {
        this.name = name;
        this.placeID = placeID;
        this.maleCount = maleCount;
        this.femaleCount = femaleCount;
        this.placeAddress = placeAddress;
    }
    public PlaceInformation(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }


}
