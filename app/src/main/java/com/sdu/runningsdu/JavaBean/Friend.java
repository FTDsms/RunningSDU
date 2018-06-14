package com.sdu.runningsdu.JavaBean;

import android.graphics.Bitmap;

import me.zhouzhuo.zzletterssidebar.anotation.Letter;
import me.zhouzhuo.zzletterssidebar.entity.SortModel;

/**
 * Created by FTDsm on 2018/5/12.
 */

public class Friend extends SortModel{

    private String sid;

    //仿微信联系人字母导航
    @Letter(isSortField = true)
    private String name;

    private String imagePath;

    private Bitmap image;

    private int unread;

    public Friend() {

    }

    public Friend(String sid, String name, String imagePath) {
        this.sid = sid;
        this.name = name;
        this.imagePath = imagePath;
    }

    public Friend(String name) {
        this.name = name;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "sid='" + sid + '\'' +
                ", name='" + name + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", unread=" + unread +
                '}';
    }
}
