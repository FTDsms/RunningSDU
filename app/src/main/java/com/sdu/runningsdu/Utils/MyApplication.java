package com.sdu.runningsdu.Utils;

import android.app.Application;

import com.sdu.runningsdu.JavaBean.User;

/**
 * Created by FTDsm on 2018/5/30.
 */

public class MyApplication extends Application {

    private boolean isTest = false;

//    private String ip = "http://121.250.223.122:8080";
    private String ip = "http://192.168.0.113:8080";

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean isTest() {
        return isTest;
    }

    public void setTest(boolean test) {
        isTest = test;
    }
}
