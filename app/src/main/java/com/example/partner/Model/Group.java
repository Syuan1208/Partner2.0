package com.example.partner.Model;

public class Group {
    private String creater;
    private String name;
    private String imageURL;
    private String rollcall;
    private String currenttime;
    public Group(String creater, String name,String imageURL,String rollcall,String currenttime) {
        this.creater = creater;
        this.name = name;
        this.imageURL=imageURL;
        this.rollcall=rollcall;
        this.currenttime=currenttime;
    }
    public Group(){

    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getRollcall() {
        return rollcall;
    }

    public void setRollcall(String rollcall) {
        this.rollcall = rollcall;
    }

    public String getCurrenttime() {
        return currenttime;
    }

    public void setCurrenttime(String currenttime) {
        this.currenttime = currenttime;
    }
}
