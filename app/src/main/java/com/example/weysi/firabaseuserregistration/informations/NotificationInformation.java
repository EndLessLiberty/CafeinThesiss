package com.example.weysi.firabaseuserregistration.informations;

/**
 * Created by Enes on 26.04.2018.
 */

public class NotificationInformation {
    private String from;
    private String type;
    private Long time;
    private String result;

    public NotificationInformation(String from,String result, Long time, String type) {
        this.from = from;
        this.type = type;
        this.time = time;
        this.result = result;
    }

    public NotificationInformation()
    {

    }


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
