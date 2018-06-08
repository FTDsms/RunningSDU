package com.sdu.runningsdu.JavaBean;

import java.util.List;

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

    private String image; //TODO: head_image

    private List<Message> messages;

    private int unread;

    public Friend() {

    }

    public Friend(String sid, String name, String image) {
        this.sid = sid;
        this.name = name;
        this.image = image;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }
}
