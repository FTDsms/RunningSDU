package com.sdu.runningsdu.JavaBean;

import java.util.List;

/**
 * Created by FTDsm on 2018/5/14.
 */

public class Group {

    private String gid;

    private String name;

    private String owner;

    private List<String> admin;

    private List<String> members;

    private List<Message> messages;

    private String image;

    private int unread;

    public Group() {

    }

    public Group(String name) {
        this.name = name;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String id) {
        this.gid = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<String> getAdmin() {
        return admin;
    }

    public void setAdmin(List<String> admin) {
        this.admin = admin;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }
}
