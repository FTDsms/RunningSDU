package com.sdu.runningsdu.JavaBean;

/**
 * Created by FTDsm on 2018/5/14.
 */

public class Message {

    private int mid;

    private boolean isGroup;

    private String friend;

    private int group;

    //时间
    private String time;

    //消息类型
    private int type;

    //接收或发送
    public static final int TYPE_RECEIVED = 0;
    public static final int TYPE_SENT = 1;

    //文字、语音、图片、文件
//    public static final int TYPE_TEXT = 10;
//    public static final int TYPE_VOICE = 11;
//    public static final int TYPE_PICTURE = 12;
//    public static final int TYPE_FILE = 13;

    //文字内容
    private String content;
    //语音
    private String voice;
    //图片
    private String picture;
    //文件
    private String file;

    // 好友消息
    public Message(int mid, String friend, int type, String content, String time) {
        this.mid = mid;
        this.isGroup = false;
        this.friend = friend;
        this.type = type;
        this.content = content;
        this.time = time;
    }

    // 群聊消息
    public Message(int mid, int group, String friend, int type, String content, String time) {
        this.mid = mid;
        this.isGroup = true;
        this.group = group;
        this.friend = friend;
        this.type = type;
        this.content = content;
        this.time = time;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public String getFriend() {
        return friend;
    }

    public void setFriend(String friend) {
        this.friend = friend;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Message{" +
                "mid=" + mid +
                ", isGroup=" + isGroup +
                ", friend='" + friend + '\'' +
                ", group=" + group +
                ", time='" + time + '\'' +
                ", type=" + type +
                ", content='" + content + '\'' +
                '}';
    }
}
