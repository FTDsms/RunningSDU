package com.sdu.runningsdu.JavaBean;

/**
 * Created by FTDsm on 2018/6/5.
 */

public class Request {

    private String rid;

    private String receiver;

    private String sender;

    private String time;

    private String state;

    public static final int WAIT_FOR_REPLY = 0;

    public static final int ACCEPTED = 1;

    public static final int REFUSED = -1;

    public Request() {

    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
