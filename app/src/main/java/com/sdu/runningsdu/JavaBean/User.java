package com.sdu.runningsdu.JavaBean;

import java.util.List;

/**
 * Created by FTDsm on 2018/5/14.
 */

public class User {

    private String sid;

    private String name;

    private String password;

    private String image; //TODO: head_image

    private List<Friend> friends;

    private List<Group> groups;

    public User() {

    }

    public User(String sid, String name, String password, String image) {
        this.sid = sid;
        this.name = name;
        this.password = password;
        this.image = image;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Friend> getFriends() {
        return friends;
    }

    public void setFriends(List<Friend> friends) {
        this.friends = friends;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return "User{" +
                "sid='" + sid + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
