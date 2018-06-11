package com.sdu.runningsdu.JavaBean;

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

    private List<Message> messages;

    private String image;

    private int unread;

    public Group() {

    }

    public Group(int gid, String name, String creator, String image) {
        this.gid = gid;
        this.name = name;
        this.creator = creator;
        this.image = image;
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

    @Override
    public String toString() {
        return "Group{" +
                "gid=" + gid +
                ", name='" + name + '\'' +
                ", creator='" + creator + '\'' +
                ", image='" + image + '\'' +
                ", unread=" + unread +
                '}';
    }
}
