package com.example.weysi.firabaseuserregistration.informations;

/**
 * Created by weysi on 6.11.2017.
 */

public class PlaceInformation {
    private String placeID;
    private String name;

    public PlaceInformation(String name, String placeID) {
        this.name = name;
        this.placeID = placeID;
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
