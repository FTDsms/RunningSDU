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

    public static void syncUser(MyApplication myApplication, MyDAO myDAO) {
        String ip = myApplication.getIp();
        User user = myApplication.getUser();
        String sid = myApplication.getUser().getSid();
        if (!myDAO.hasUser() || !myDAO.findUser(sid).getSid().equals(sid)) {
            // if user not exists, add user
            myDAO.addUser(user);
        } else {
            // if user exists, update user info
            myDAO.updateUser(user);
        }
        // sync user image
        try {
            String imagePath = user.getImagePath();
            Log.e("bytes", imagePath);
            if (!myDAO.equalsUserImagePath(sid, imagePath)) {
                // if server path is not equals local path
                // then download new image and update
                byte[] bytes = MyHttpClient.downloadImage(ip, imagePath);
                Log.e("bytes length", bytes.length+"");
                myDAO.updateUserImage(sid, imagePath, bytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 同步好友
     * */
    public static void syncFriend(MyApplication myApplication, MyDAO myDAO) {
        String ip = myApplication.getIp();
        User user = myApplication.getUser();
        try {
            List<Friend> friends = MyHttpClient.findMyFriend(ip, user.getSid());
            if ((friends != null) && (friends.size() > 0)) {
                for (Friend friend : friends) {
                    if (!myDAO.hasFriend(friend.getSid())) {
                        // if friend not exists, add friend
                        myDAO.addFriend(friend);
                    } else {
                        // if friend exists, update friend
                        myDAO.updateFriend(friend);
                    }
                    // sync friend image
                    String sid = friend.getSid();
                    String imagePath = friend.getImagePath();
                    if (!myDAO.equalsFriendImagePath(sid, imagePath)) {
                        // if server path is not equals local path
                        // then download new image and update
                        byte[] bytes = MyHttpClient.downloadImage(ip, imagePath);
                        myDAO.updateFriendImage(sid, imagePath, bytes);
                    }
                }
            } else {
                // if list == null or size <= 0
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 同步群组
     * */
    public static void syncGroup(MyApplication myApplication, MyDAO myDAO) {
        String ip = myApplication.getIp();
        String sid = myApplication.getUser().getSid();
        try {
            List<Group> groups = MyHttpClient.findMyGroup(ip, sid);
            if ((groups != null) && (groups.size() > 0)) {
                for (Group group : groups) {
                    if (!myDAO.hasGroup(group.getGid())) {
                        // if group not exists, add group
                        myDAO.addGroup(group);
                        // update members
                        List<String> members = group.getMembers();
                        for (String member : members) {
                            if (!myDAO.hasGroupMember(group.getGid(), member)) {
                                myDAO.addGroupMember(group.getGid(), member);
                            }
                        }
                    } else {
                        // if group exists, update group
                        myDAO.updateGroup(group);
                        // update members
                        List<String> members = group.getMembers();
                        for (String member : members) {
                            if (!myDAO.hasGroupMember(group.getGid(), member)) {
                                myDAO.addGroupMember(group.getGid(), member);
                            }
                        }
                    }
                    // sync group image
                    int gid = group.getGid();
                    String imagePath = group.getImagePath();
                    if (!myDAO.equalsGroupImagePath(gid, imagePath)) {
                        // if server path is not equals local path
                        // then download new image and update
                        byte[] bytes = MyHttpClient.downloadImage(ip, imagePath);
                        myDAO.updateGroupImage(gid, imagePath, bytes);
                    }
                }
            } else {
                // if list == null or size <= 0
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 同步好友申请
     * */
    public static void syncRequest(MyApplication myApplication, MyDAO myDAO) {
        String ip = myApplication.getIp();
        String sid = myApplication.getUser().getSid();
        try {
            List<Request> requests = MyHttpClient.findReceivedRequest(ip, sid);
            if ((requests != null) && (requests.size() > 0)) {
                for (Request request : requests) {
                    if (!myDAO.hasRequest(request.getRid())) {
                        // if request not exists, add request
                        myDAO.addRequest(request);
                    } else {
                        // if request exists, update request
                        myDAO.updateRequest(request);
                    }
                }
            } else {
                // if list == null or size <= 0
            }
            // set user to MyApplication
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 同步好友消息
     * */
    public static void syncFriendMessage(MyApplication myApplication, MyDAO myDAO) {
        String ip = myApplication.getIp();
        String sid = myApplication.getUser().getSid();
        List<Friend> friends = myDAO.findAllFriend();
        try {
            for (Friend friend : friends) {
                int mid = myDAO.findLastFriendMessage(friend.getSid());
                List<Message> messages = MyHttpClient.findFriendMessage(ip, mid, sid, friend.getSid());
                if ((messages != null) && (messages.size() > 0)) {
                    for (Message message : messages) {
                        if (!myDAO.hasFriendMessage(message)) {
                            myDAO.addFriendMessage(message);
                        }
                    }
                    friend.setUnread(friend.getUnread()+messages.size()); //设置未读消息
                    myDAO.updateFriendUnread(friend);
                } else {
                    // if list == null or size <= 0
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 同步群组消息
     * */
    public static void syncGroupMessage(MyApplication myApplication, MyDAO myDAO) {
        String ip = myApplication.getIp();
        String sid = myApplication.getUser().getSid();
        List<Group> groups = myDAO.findAllGroup();
        try {
            for (Group group : groups) {
                int mid = myDAO.findLastGroupMessage(group.getGid());
                List<Message> messages = MyHttpClient.findGroupMessage(ip, group.getGid(), mid, sid);
                if ((messages != null) && (messages.size() > 0)) {
                    for (Message message : messages) {
                        if (!myDAO.hasGroupMessage(message)) {
                            myDAO.addGroupMessage(message);
                        }
                    }
                    group.setUnread(group.getUnread()+messages.size()); //设置未读消息
                    myDAO.updateGroupUnread(group);
                } else {
                    // if list == null or size <= 0
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 同步用户头像
     * */
    public static void syncUserImage(MyApplication myApplication, MyDAO myDAO) {
        String ip = myApplication.getIp();
        String sid = myApplication.getUser().getSid();
        try {
            String imagePath = MyHttpClient.findUserBySid(ip, sid).getImagePath();
            Log.e("bytes", imagePath);
            if (!myDAO.equalsUserImagePath(sid, imagePath)) {
                // if server path is not equals local path
                // then download new image and update
                byte[] bytes = MyHttpClient.downloadImage(ip, imagePath);
                Log.e("bytes length111", bytes.length+"");
                myDAO.updateUserImage(sid, imagePath, bytes);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 同步好友头像
     * */
    public static void syncFriendImage(MyApplication myApplication, MyDAO myDAO) {
        String ip = myApplication.getIp();
        List<Friend> friends = myDAO.findAllFriend();
        try {
            for (Friend friend : friends) {
                String sid = friend.getSid();
                String imagePath = MyHttpClient.findUserBySid(ip, sid).getImagePath();
                if (!myDAO.equalsFriendImagePath(sid, imagePath)) {
                    // if server path is not equals local path
                    // then download new image and update
                    byte[] bytes = MyHttpClient.downloadImage(ip, imagePath);
                    myDAO.updateFriendImage(sid, imagePath, bytes);
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 同步群组头像
     * */
    public static void syncGroupImage(MyApplication myApplication, MyDAO myDAO) {
        String ip = myApplication.getIp();
        List<Group> groups = myDAO.findAllGroup();
        try {
            for (Group group : groups) {
                int gid = group.getGid();
                String imagePath = MyHttpClient.findGroupByGid(ip, gid).getImagePath();
                if (!myDAO.equalsGroupImagePath(gid, imagePath)) {
                    // if server path is not equals local path
                    // then download new image and update
                    byte[] bytes = MyHttpClient.downloadImage(ip, imagePath);
                    myDAO.updateGroupImage(gid, imagePath, bytes);
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }


}
