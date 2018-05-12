package com.example.weysi.firabaseuserregistration.informations;

import android.graphics.Bitmap;

/**
 * Created by lenovo on 1.11.2017.
 */

public class Data {
    private String name;
    private String url;
    private String PlaceId;
    private String address;
    private Bitmap bitmap ;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPlaceId() {
        return PlaceId;
    }

    public void setPlaceId(String placeId) {
        PlaceId = placeId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
