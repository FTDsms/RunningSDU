package com.sdu.runningsdu.Utils;

import android.util.Log;

import com.sdu.runningsdu.JavaBean.Friend;
import com.sdu.runningsdu.JavaBean.Group;
import com.sdu.runningsdu.JavaBean.Message;
import com.sdu.runningsdu.JavaBean.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by FTDsm on 2018/6/7.
 * Data synchronization
 */

public class DataSync {

    /**
     * 获取好友
     * */
    public static List<Friend> getFriends(String ip, String sid) throws IOException, JSONException {
        List<Friend> friends = new ArrayList<>();
        String response = MyHttpClient.findFriend(ip, sid);
        Log.w("test", response);
        JSONArray json = new JSONArray(response);
        for (int i=0; i<json.length(); ++i) {
            JSONObject obj = json.optJSONObject(i);
            String fsid = obj.optString("sid");
            String name = obj.optString("name");
            String image = obj.optString("image");
            Friend friend = new Friend(fsid, name, image);
            friends.add(friend);
        }
        return friends;
    }

    /**
     * 获取群组
     * */
    public static List<Group> getGroups(String ip, String gid) throws IOException, JSONException {
        List<Group> groups = new ArrayList<>();
        String response = MyHttpClient.findMyGroup(ip, gid);
        Log.w("test", response);
        JSONArray json = new JSONArray(response);
        for (int i=0; i<json.length(); ++i) {
            JSONObject obj = json.optJSONObject(i);
            String ggid = obj.optString("gid");
            String name = obj.optString("name");
            String creator = obj.optString("creator");
            String image = obj.optString("image");
            Group group = new Group(ggid, name, creator, image);
            groups.add(group);
        }
        return groups;
    }

    /**
     * 获取好友消息
     * */
    public static List<Message> getFriendMessage(String ip, int mid, String myName, String friendName) throws IOException, JSONException {
        List<Message> messages = new ArrayList<>();
        String response = MyHttpClient.findMessage(ip, mid, myName, friendName);
        Log.w("test", response);
        JSONObject json = new JSONObject(response);
        String flag = json.optString("flag");
        if (!flag.equals("true")) {
            // failure
            String msg = json.optString("msg");
            if (msg.equals("wrong password")) {

            }
            if (msg.equals("no such student")) {

            }
            return null;
        } else {
            // success
            JSONObject obj = json.optJSONObject("obj");
            for (int i=0; i<obj.length(); ++i) {
                int mmid = Integer.parseInt(obj.optString("coid"));
                String sender = obj.optString("sender");
                String receiver = obj.optString("receiver");
                String content = obj.optString("content");
                String time = obj.optString("time");
                String friend;
                int type;
                if (sender.equals(myName)) {
                    friend = receiver;
                    type = Message.TYPE_SENT;
                } else {
                    friend = sender;
                    type = Message.TYPE_RECEIVED;
                }
                Message message = new Message(mmid, friend, type, content, time);
                messages.add(message);
            }
            return messages;
        }
    }

    /**
     * 获取群组消息
     * */
    public static List<Message> getGroupMessage(String ip, String gid, int mid, String myName) throws IOException, JSONException {
        List<Message> messages = new ArrayList<>();
        String response = MyHttpClient.findGroupMessage(ip, gid, mid);
        Log.w("test", response);
        JSONObject json = new JSONObject(response);
        String flag = json.optString("flag");
        if (!flag.equals("true")) {
            // failure
            String msg = json.optString("msg");
            if (msg.equals("wrong password")) {

            }
            if (msg.equals("no such student")) {

            }
            return null;
        } else {
            // success
            JSONObject obj = json.optJSONObject("obj");
            for (int i=0; i<obj.length(); ++i) {
                int gnid = Integer.parseInt(obj.optString("gnid"));
                String sid = obj.optString("sid");
                String content = obj.optString("content");
                String time = obj.optString("time");
                int type;
                if (sid.equals(myName)) {
                    type = Message.TYPE_SENT;
                } else {
                    type = Message.TYPE_RECEIVED;
                }
                Message message = new Message(gnid, gid, sid, type, content, time);
                messages.add(message);
            }
            return messages;
        }
    }

    /**
     * 获取好友申请
     * */
    public static List<Request> getRequest(String ip, String receiver) throws IOException, JSONException {
        List<Request> requests = new ArrayList<>();
        String response = MyHttpClient.findReceivedRequest(ip, receiver);
        Log.w("test", response);
        JSONArray json = new JSONArray(response);
        for (int i=0; i<json.length(); ++i) {
            JSONObject obj = json.optJSONObject(i);
            String rid = obj.optString("rid");
            String sender = obj.optString("sender");
            String message = obj.optString("message");
            String time = obj.optString("time");
            int state = Integer.parseInt(obj.optString("state"));
            Request request = new Request(rid, receiver, sender, message, time, state);
            requests.add(request);
        }
        return requests;
    }

}
