package com.example.weysi.firabaseuserregistration.informations;

/**
 * Created by weysi on 6.11.2017.
 */

public class UserInformation {
    private String name;
    private String phone;
    private String userID;
    private String nickName;
    private String cinsiyet;
    private String dogumTarihi;
    private String durum;
    private String image;
    private String thumb_image;
    private String device_token;



    public UserInformation(String name, String phone, String userID,String nickName, String cinsiyet, String dogumTarihi, String durum, String image, String thumb_image, String device_token ) {
        this.name = name;
        this.phone = phone;
        this.userID = userID;
        this.nickName=nickName;
        this.cinsiyet=cinsiyet;
        this.dogumTarihi=dogumTarihi;
        this.durum=durum;
        this.image=image;
        this.thumb_image=thumb_image;
        this.device_token=device_token;
    }
    public  UserInformation(){

    }


    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

    public String getDogumTarihi() {
        return dogumTarihi;
    }

    public void setDogumTarihi(String dogumTarihi) {
        this.dogumTarihi = dogumTarihi;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDurum() {
        return durum;
    }

    public void setDurum(String durum) {
        this.durum = durum;
    }
}
