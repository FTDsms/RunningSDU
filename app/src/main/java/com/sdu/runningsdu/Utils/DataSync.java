package com.sdu.runningsdu.Utils;

import android.util.Log;

import com.sdu.runningsdu.JavaBean.Friend;
import com.sdu.runningsdu.JavaBean.Group;
import com.sdu.runningsdu.JavaBean.Message;
import com.sdu.runningsdu.JavaBean.Request;
import com.sdu.runningsdu.JavaBean.User;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by FTDsm on 2018/6/7.
 * Data synchronization
 */

public class DataSync {

    /**
     * 同步好友
     * */
    public static void syncFriend(MyApplication myApplication, MyDAO myDAO) {
        User user = myApplication.getUser();
        String ip = myApplication.getIp();
        String sid = user.getSid();
        try {
            List<Friend> friends = MyHttpClient.findMyFriend(ip, sid);
            if ((friends != null) && (friends.size() > 0)) {
                for (Friend friend : friends) {
                    if (!myDAO.hasFriend(friend.getSid())) {
                        // if friend not exists, add friend
                        myDAO.addFriend(friend);
                    } else {
                        // if friend exists, update friend
                        myDAO.updateFriend(friend);
                    }
                }
                // set friends to user
                user.setFriends(friends);
            } else {
                // if list == null or size <= 0
                user.setFriends(new ArrayList<Friend>());
            }
            // set user to MyApplication
            myApplication.setUser(user);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 同步群组
     * */
    public static void syncGroup(MyApplication myApplication, MyDAO myDAO) {
        User user = myApplication.getUser();
        String ip = myApplication.getIp();
        String sid = user.getSid();
        try {
            List<Group> groups = MyHttpClient.findMyGroup(ip, sid);
            if ((groups != null) && (groups.size() > 0)) {
                for (Group group : groups) {
                    if (!myDAO.hasGroup(group.getGid())) {
                        // if friend not exists, add friend
                        myDAO.addGroup(group);
                        myDAO.addGroupMember(group);
                    } else {
                        // if friend exists, update friend
                        myDAO.updateGroup(group);
                        myDAO.updateGroupMember(group);
                    }
                }
                // set friends to user
                user.setGroups(groups);
            } else {
                // if list == null or size <= 0
                user.setFriends(new ArrayList<Friend>());
            }
            // set user to MyApplication
            myApplication.setUser(user);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 同步好友申请
     * */
    public static void syncRequest(MyApplication myApplication, MyDAO myDAO) {
        User user = myApplication.getUser();
        String ip = myApplication.getIp();
        String sid = user.getSid();
        try {
            List<Request> requests = MyHttpClient.findReceivedRequest(ip, sid);
            if ((requests != null) && (requests.size() > 0)) {
                for (Request request : requests) {
                    if (!myDAO.hasRequest(request.getRid())) {
                        // if friend not exists, add friend
                        myDAO.addRequest(request);
                    } else {
                        // if friend exists, update friend
                        myDAO.updateRequest(request);
                    }
                }
                // set friends to user
                user.setRequests(requests);
            } else {
                // if list == null or size <= 0
                user.setRequests(new ArrayList<Request>());
            }
            // set user to MyApplication
            myApplication.setUser(user);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 同步好友消息
     * */
    public static void syncFriendMessage(MyApplication myApplication, MyDAO myDAO) {
        User user = myApplication.getUser();
        String ip = myApplication.getIp();
        String sid = user.getSid();
        List<Friend> friends = user.getFriends();
        try {
            for (Friend friend : friends) {
                int mid = myDAO.findLastFriendMessage(friend.getSid());
                List<Message> messages = MyHttpClient.findFriendMessage(ip, mid, sid, friend.getSid());
                if ((messages != null) && (messages.size() > 0)) {
                    // set messages to friend
                    friend.setMessages(messages);
                    myDAO.addFriendMessages(messages);
                    friend.setUnread(friend.getUnread()+messages.size()); //设置未读消息
                    myDAO.updateFriendUnread(friend);
                } else {
                    // if list == null or size <= 0
                    user.setFriends(new ArrayList<Friend>());
                }
            }
            // set friends to user
            user.setFriends(friends);
            // set user to MyApplication
            myApplication.setUser(user);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 同步群组消息
     * */
    public static void syncGroupMessage(MyApplication myApplication, MyDAO myDAO) {
        User user = myApplication.getUser();
        String ip = myApplication.getIp();
        String sid = user.getSid();
        List<Group> groups = user.getGroups();
        try {
            for (Group group : groups) {
                int mid = myDAO.findLastGroupMessage(group.getGid());
                List<Message> messages = MyHttpClient.findGroupMessage(ip, group.getGid(), mid, sid);
                if ((messages != null) && (messages.size() > 0)) {
                    // set messages to group
                    group.setMessages(messages);
                    myDAO.addFriendMessages(messages);
                    group.setUnread(group.getUnread()+messages.size()); //设置未读消息
                    myDAO.updateGroupUnread(group);
                } else {
                    // if list == null or size <= 0
                    user.setFriends(new ArrayList<Friend>());
                }
            }
            // set groups to user
            user.setGroups(groups);
            // set user to MyApplication
            myApplication.setUser(user);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }


}
