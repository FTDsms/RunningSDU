package com.sdu.runningsdu.JavaBean;

import android.graphics.Bitmap;

import java.util.List;

import me.zhouzhuo.zzletterssidebar.anotation.Letter;
import me.zhouzhuo.zzletterssidebar.entity.SortModel;

/**
 * Created by FTDsm on 2018/5/14.
 */

public class Group extends SortModel {

    private int gid;

    // 按群名排序
    @Letter(isSortField = true)
    private String name;

    private String creator;

    private List<String> members;

    private String imagePath;

    private Bitmap image;

    private int unread;

    public Group() {

    }

    public Group(int gid, String name, String creator, String imagePath) {
        this.gid = gid;
        this.name = name;
        this.creator = creator;
        this.imagePath = imagePath;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int id) {
        this.gid = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
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
        return "Group{" +
                "gid=" + gid +
                ", name='" + name + '\'' +
                ", creator='" + creator + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", unread=" + unread +
                '}';
    }
}
