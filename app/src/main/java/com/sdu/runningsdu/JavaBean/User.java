package com.sdu.runningsdu.JavaBean;

import android.graphics.Bitmap;

/**
 * Created by FTDsm on 2018/5/14.
 */

public class User {

    private String sid;

    private String name;

    private String password;

    private String imagePath; //TODO: head_image

    private Bitmap image;

    public User() {

    }

    public User(String sid, String name, String password, String imagePath) {
        this.sid = sid;
        this.name = name;
        this.password = password;
        this.imagePath = imagePath;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "User{" +
                "sid='" + sid + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}
