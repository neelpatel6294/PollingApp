package com.example.android.PollingApp;



public class Pojo {

    private String title, desc, imageUrl1,imageUrl2, username;

    public Pojo(String title, String desc, String imageUrl1,String imageUrl2, String username) {
        this.title = title;
        this.desc = desc;
        this.imageUrl1 = imageUrl1;
        this.username = username;
        this.imageUrl2 = imageUrl2;
    }

    public Pojo() {
    }

    public String getImageUrl2() {
        return imageUrl2;
    }

    public void setImageUrl2(String imageUrl2) {
        this.imageUrl2 = imageUrl2;
    }

    public void setImageUrl1(String imageUrl1) {
        this.imageUrl1 = imageUrl1;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImageUrl1() {
        return imageUrl1;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

}
