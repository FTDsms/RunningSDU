package com.sdu.runningsdu;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.sdu.runningsdu.JavaBean.Friend;
import com.sdu.runningsdu.JavaBean.Group;
import com.sdu.runningsdu.JavaBean.Message;
import com.sdu.runningsdu.JavaBean.User;

/**
 * Created by FTDsm on 2018/6/4.
 */

public class MyDAO {

    private Context context;

    private String name;

    private DatabaseHelper databaseHelper;

    public MyDAO(Context context, String name) {
        this.context = context;
        this.name = name;
        databaseHelper = new DatabaseHelper(context, name);
    }

    //添加用户
    public void addUser(User user) {
        SQLiteDatabase db = this.databaseHelper.getWritableDatabase();
        Object[] objects = new Object[4];
        objects[0] = user.getSid();
        objects[1] = user.getName();
        objects[2] = user.getPassword();
        objects[3] = user.getImage();
        String sql = "insert into user(sid, name, password, image) values(?,?,?,?)";
        db.execSQL(sql, objects);
        db.close();
    }

    //修改用户信息
    public void updateUser(User user) {

    }

    //查找所有好友
    public void findAllFriend() {

    }

    //添加好友
    public void addFriend(Friend friend) {

    }

    //删除好友
    public void deleteFriend(String sid) {

    }

    //查找所有群聊
    public void findAllGroup() {

    }

    //添加群聊
    public void addGroup(Group group) {

    }

    //退群
    public void deleteGroup(String gid) {

    }

    //查找好友消息
    public void findFriendMessage(String sid) {

    }

    //查找群聊消息
    public void findGroupMessage(String gid) {

    }

    //添加消息
    public void addMessage(Message message) {

    }

    //删除消息
    public void deleteMessage(String mid) {

    }

}
