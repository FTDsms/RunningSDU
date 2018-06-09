package com.sdu.runningsdu.JavaBean;

/**
 * Created by FTDsm on 2018/6/5.
 */

public class Request {

    private int rid;

    private String receiver;

    private String sender;

    private String message;

    private String time;

    private int state;

    public static final int WAIT_FOR_REPLY = 0;

    public static final int ACCEPTED = 1;

    public static final int REFUSED = -1;

    // 接收
    public Request(int rid, String receiver, String sender, String message, String time, int state) {
        this.rid = rid;
        this.receiver = receiver;
        this.sender = sender;
        this.message = message;
        this.time = time;
        this.state = state;
    }

    // 发送
    public Request(String receiver, String sender, String message, String time, String state) {
        this.receiver = receiver;
        this.sender = sender;
        this.message = message;
        this.time = time;
        this.state = Request.WAIT_FOR_REPLY;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
